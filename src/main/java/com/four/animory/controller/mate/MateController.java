package com.four.animory.controller.mate;

import com.four.animory.config.auth.PrincipalDetails;

import com.four.animory.dto.mate.*;
import com.four.animory.dto.mate.upload.UploadFileDTO;

import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.mate.MateService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/mate")

public class MateController {

    @Value("${com.four.animory.upload.path}")
    private String uploadPath;

    @Autowired
    private MateService mateService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public void replyCountList(@AuthenticationPrincipal PrincipalDetails principal, MatePageRequestDTO matePageRequestDTO, Model model) {
        MemberDTO loginUser = null;
        if(principal != null){
            loginUser = userService.getMemberByUsername(principal.getMember().getUsername());
            int petCount = userService.getPetListByMemberId(loginUser.getMid()).size();
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("petCount", petCount);
        }
        log.info("replyCountList");
        MatePageResponseDTO<MateReplyCountDTO> responseDTO = mateService.getListReplyCount(matePageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/view")
    public void view(Long bno, Integer mode, Model model) {
        if (mode == null) mode = 1;
        model.addAttribute("board", mateService.findMateBoardById(bno, mode));
    }

    @GetMapping("/register")
    public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
      if(principal != null) {
        MemberDTO memberDTO = userService.getMemberByUsername(principal.getMember().getUsername());
        model.addAttribute("memberDTO", memberDTO);
      }
      log.info("registerGet");
    }

    @PostMapping("/register")
    public String registerPost(UploadFileDTO uploadFileDTO, MateBoardDTO mateBoardDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MateFileDTO> fileDTOS = null;
        if (uploadFileDTO.getFiles() != null &&

                !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
            fileDTOS = fileUpload(uploadFileDTO);
        }
        mateBoardDTO.setMateFileDTOs(fileDTOS);
        mateBoardDTO.setUsername(principalDetails.getUsername());
        log.info("registerPost mataBoardDTO"+mateBoardDTO);
        Long bno = mateService.registerMateBoard(mateBoardDTO);
        log.info("board insert success : bno=" + bno);
        return "redirect:/mate/list";
    }

    private List<MateFileDTO> fileUpload(UploadFileDTO uploadFileDTO) {
        String mateUploadPath = uploadPath + "\\mate";
        List<MateFileDTO> list = new ArrayList<>();
        if (uploadFileDTO.getFiles() != null) {
            uploadFileDTO.getFiles().forEach(multifile -> {
                String originalFileName = multifile.getOriginalFilename();
                log.info("originalFileName" + originalFileName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(mateUploadPath, uuid + "_" + originalFileName);
                boolean image = false;
                try {
                    multifile.transferTo(savePath);
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbnail = new File(mateUploadPath, "s_" + uuid + "_" + originalFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//
                MateFileDTO mateFileDTO = MateFileDTO.builder()
                        .uuid(uuid)
                        .filename(originalFileName)
                        .image(image)
                        .build();
                list.add(mateFileDTO);
            });
        }
        return list;
    }


    public void removeFile(List<MateFileDTO> fileDTOS) {
        String mateUploadPath = uploadPath + "\\mate";
        for (MateFileDTO mateFileDTO : fileDTOS) {
            String filename = mateFileDTO.getUuid() + "_" + mateFileDTO.getFilename();
            Resource resource = new FileSystemResource(
                    mateUploadPath + File.separator + filename);
            String resourceName = resource.getFilename();
            boolean removed = false;
            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();
                if (contentType.startsWith("image")) {
                    String fileName1 = "s_" + filename;
                    File thumFile = new File(mateUploadPath + File.separator + fileName1);
                    thumFile.delete(); //원본 파일이 지워져
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/modify")
    public String modify(Long bno, Integer mode, Model model) {
        log.info("모드 확인:" + mode);
        MateBoardDTO mateBoardDTO = mateService.findMateBoardById(bno, mode);
        model.addAttribute("mateBoard", mateBoardDTO);
        return "mate/modify";
    }

    @PostMapping("/modify")
    public String modify(MateBoardDTO mateBoardDTO) {
        mateService.updateMateBoard(mateBoardDTO);
        return "redirect:/mate/view?bno=" + mateBoardDTO.getBno();
    }

    @GetMapping("remove")
    public String removeMateBoard(Long bno) {
        mateService.deleteMateBoardById(bno);
        return "redirect:/mate/list";
    }

    @PostMapping(value = "/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> like(@RequestBody Map<String, Long> payload) {
        Long bno = payload.get("bno");
        int updated = mateService.increaseLikeCountAndGet(bno);
        return Map.of("likecount", updated);
    }

    @GetMapping("/top10-test")
    @ResponseBody
    public List<MateBoardDTO> getTop10Test() {
        List<MateBoardDTO> list = mateService.getTop10MateBoardList();
        return list;
    }

}