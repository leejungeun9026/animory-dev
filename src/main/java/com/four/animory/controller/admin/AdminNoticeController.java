package com.four.animory.controller.admin;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.dto.notice.NoticeFileDTO;
import com.four.animory.dto.notice.upload.UploadNoticeFileDTO;
import com.four.animory.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @Value("${com.four.animory.upload.path}") // 예: C:/upload (실제 저장은 C:/upload/notice)
    private String uploadRootPath;

    /* ================= 등록 ================= */
    @GetMapping("/register")
    public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        if (principal != null) {
            Member member = principal.getMember();
            log.info("관리자 로그인 정보: {}", member.getNickname());
            model.addAttribute("nickname", member.getNickname());
        } else {
            log.info("비로그인 상태에서 관리자 공지 등록 페이지 접근");
        }
    }

    @PostMapping("/register")
    public String registerPost(@AuthenticationPrincipal PrincipalDetails principal,
                               NoticeBoardDTO dto,
                               @ModelAttribute("files") UploadNoticeFileDTO uploadNoticeDTO) {
        if (principal != null) {
            dto.setNickname(principal.getMember().getNickname());
        }

        List<NoticeFileDTO> files = null;
        if (uploadNoticeDTO != null && uploadNoticeDTO.getFiles() != null
                && !uploadNoticeDTO.getFiles().isEmpty()
                && uploadNoticeDTO.getFiles().get(0).getOriginalFilename() != null
                && !uploadNoticeDTO.getFiles().get(0).getOriginalFilename().isBlank()) {
            files = fileUpload(uploadNoticeDTO);
        }

        dto.setNoticeFileDTOs(files);
        noticeService.registerNotice(dto);
        return "redirect:/admin/notice/list";
    }

    /* ================= 목록 ================= */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        var responseDTO = noticeService.getList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("list", responseDTO.getDtoList());
    }

    /* ================= 보기 ================= */
    @GetMapping("/view")
    public String view(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 1));
        return "admin/notice/view";
    }

    /* ================= 수정 ================= */
    @GetMapping("/modify")
    public String modifyForm(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 0));
        return "admin/notice/modify";
    }

    @PostMapping("/modify")
    public String modifyPost(NoticeBoardDTO noticeBoardDTO,
                             @ModelAttribute("files") UploadNoticeFileDTO uploadNoticeDTO,
                             @RequestParam(value = "deleteFiles", required = false) List<String> deleteFiles,
                             RedirectAttributes redirectAttributes) {

        NoticeBoardDTO original = noticeService.findNoticeById(noticeBoardDTO.getBno(), 2);
        List<NoticeFileDTO> current = new ArrayList<>();
        if (original.getNoticeFileDTOs() != null) current.addAll(original.getNoticeFileDTOs());

        // 삭제 처리
        if (deleteFiles != null && !deleteFiles.isEmpty() && !current.isEmpty()) {
            List<NoticeFileDTO> toDelete = current.stream()
                    .filter(f -> deleteFiles.contains(f.getUuid()))
                    .toList();
            removeFile(toDelete);
            current.removeAll(toDelete);
        }


        if (uploadNoticeDTO != null && uploadNoticeDTO.getFiles() != null
                && !uploadNoticeDTO.getFiles().isEmpty()
                && uploadNoticeDTO.getFiles().get(0).getOriginalFilename() != null
                && !uploadNoticeDTO.getFiles().get(0).getOriginalFilename().isBlank()) {
            current.addAll(fileUpload(uploadNoticeDTO));
        }

        noticeBoardDTO.setNoticeFileDTOs(current);
        noticeService.updateNotice(noticeBoardDTO);

        redirectAttributes.addAttribute("bno", noticeBoardDTO.getBno());
        redirectAttributes.addAttribute("mode", 1);
        return "redirect:/admin/notice/view";
    }

    /* ================= 삭제 ================= */
    @GetMapping("/remove")
    public String removeGet(@RequestParam Long bno) {
        NoticeBoardDTO dto = noticeService.findNoticeById(bno, 2);
        if (dto != null && dto.getNoticeFileDTOs() != null && !dto.getNoticeFileDTOs().isEmpty()) {
            removeFile(dto.getNoticeFileDTOs());
        }
        noticeService.removeNotice(bno);
        return "redirect:/admin/notice/list";
    }

    /* ===============파일 읽기===================*/
    @GetMapping("/uploads/{folder}/{fileName}")
    @ResponseBody
    public Resource getFile(@PathVariable String folder,
                            @PathVariable String fileName) {
        Path filePath = Paths.get(uploadRootPath, folder, fileName);
        return new FileSystemResource(filePath);
    }


    /* ================= 파일 처리 ================= */
    private List<NoticeFileDTO> fileUpload(UploadNoticeFileDTO uploadNoticeDTO) {
        String noticePath = Paths.get(uploadRootPath, "notice").toString();
        List<NoticeFileDTO> list = new ArrayList<>();

        if (uploadNoticeDTO.getFiles() != null) {
            uploadNoticeDTO.getFiles().forEach(mf -> {
                String original = mf.getOriginalFilename();
                if (original == null || original.isBlank()) return;

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(noticePath, uuid + "_" + original);

                boolean image = false;
                try {
                    Files.createDirectories(savePath.getParent());
                    mf.transferTo(savePath);

                    String ct = Files.probeContentType(savePath);
                    if (ct != null && ct.startsWith("image")) {
                        image = true;
                        File thumb = new File(noticePath, "s_" + uuid + "_" + original);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumb, 200, 200);
                    }
                } catch (Exception e) {
                    log.error("notice file upload error: {}", e.getMessage(), e);
                }

                list.add(NoticeFileDTO.builder()
                        .uuid(uuid)
                        .fileName(original)
                        .image(image)
                        .build());
            });
        }
        return list;
    }

    private void removeFile(List<NoticeFileDTO> files) {
        String noticePath = Paths.get(uploadRootPath, "notice").toString();

        for (NoticeFileDTO f : files) {
            String name = f.getUuid() + "_" + f.getFileName();
            Resource res = new FileSystemResource(noticePath + File.separator + name);
            try {
                if (!res.exists()) continue;

                String ct = Files.probeContentType(res.getFile().toPath());
                res.getFile().delete();

                if (ct != null && ct.startsWith("image")) {
                    File thumb = new File(noticePath + File.separator + "s_" + name);
                    if (thumb.exists()) thumb.delete();
                }
            } catch (Exception e) {
                log.error("notice file remove error: {}", e.getMessage(), e);
            }
        }
    }
}
