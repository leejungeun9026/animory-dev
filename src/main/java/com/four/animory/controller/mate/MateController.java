package com.four.animory.controller.mate;

import com.four.animory.config.auth.PrincipalDetails;


import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.*;
import com.four.animory.dto.mate.upload.UploadFileDTO;

import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.mate.MateReplyService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        MemberDTO memberDTO = null;
        if(principal != null){
            memberDTO = userService.getMemberByUsername(principal.getMember().getUsername());
            int petCount = userService.getPetListByMemberId(memberDTO.getMid()).size();
            model.addAttribute("memberDTO", memberDTO);
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
    public void registerGet(Model model) {
        log.info("registerGet");
    }

    @PostMapping("/register")
    public String registerPost(UploadFileDTO uploadFileDTO, MateBoardDTO mateBoardDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MateFileDTO> fileDTOS = null; //리턴형은 리스트 스트링이야 아래에 함수 만들었어
        if (uploadFileDTO.getFiles() != null && //getFiles하면 파일의 리스트를 가지고 올수있어. files라는게 있어서 null은
                // 아닌데 파일을 선택을 안해서 업로드가 안됨. 파일 없으면 글 못씀.
                !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) { //(파일 이름이 공백이 아닐때) 파일을 안넣어도 업로드됨.
            fileDTOS = fileUpload(uploadFileDTO); //맨 아래 만든 uploadDTO에서 리턴한 파일 이름 list가 가!!!!
        }
        mateBoardDTO.setMateFileDTOs(fileDTOS);
        mateBoardDTO.setUsername(principalDetails.getUsername());
        log.info("registerPost mataBoardDTO"+mateBoardDTO);
        Long bno = mateService.registerMateBoard(mateBoardDTO);
        log.info("board insert success : bno=" + bno);
        return "redirect:/mate/list"; //서비스로 넘긴다!
    }

    private List<MateFileDTO> fileUpload(UploadFileDTO uploadFileDTO) {
        List<MateFileDTO> list = new ArrayList<>();
        if (uploadFileDTO.getFiles() != null) { //파일이 있으면 실행!
            uploadFileDTO.getFiles().forEach(multifile -> { // uploadFileDTO.getFiles() 하면 파일리스나와 하나씩 꺼내서 멀티파일에 할당
                String originalFileName = multifile.getOriginalFilename();
                log.info("originalFileName" + originalFileName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFileName);
                boolean image = false;
                try {
                    multifile.transferTo(savePath);
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbnail = new File(uploadPath, "s_" + uuid + "_" + originalFileName);
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
                list.add(mateFileDTO); //파일을 db에 저장해야하니까. list에 담고. list를 리턴해줘.
//                list.add(uuid+"-"+originalFileName); 원래 이랬는데, 위의 .image가 따라다녀야해서 추가 작업을 함.
            });
        }
        return list;
    }


    public void removeFile(List<MateFileDTO> fileDTOS) {
        for (MateFileDTO mateFileDTO : fileDTOS) {
            String filename = mateFileDTO.getUuid() + "_" + mateFileDTO.getFilename();
            Resource resource = new FileSystemResource(
                    uploadPath + File.separator + filename);
            //path가 서버에 저장된 파일 이름
            String resourceName = resource.getFilename();
            boolean removed = false;
            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();
                if (contentType.startsWith("image")) {
                    String fileName1 = "s_" + filename;
                    File thumFile = new File(uploadPath + File.separator + fileName1);
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


}






