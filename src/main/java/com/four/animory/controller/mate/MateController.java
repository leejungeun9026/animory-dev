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


    //1차 list
//    @GetMapping("/list")
//    public String list(Model model) {
//        List<MateBoardDTO> mateboardList = mateService.findAllMateBoards();
//        log.info("LIST SIZE = {}", mateboardList.size());
//        model.addAttribute("mateboardList", mateboardList);
//        return "mate/list";
//    }

    // 해결필요
    @GetMapping("/list")
    public void replyCountList(MatePageRequestDTO matePageRequestDTO, Model model) {
        log.info("replyCountList");
        MatePageResponseDTO<MateReplyCountDTO> responseDTO = mateService.getListReplyCount(matePageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    //해결필요
//    public String list(MatePageRequestDTO matePageRequestDTO, Model model) {
//        PageResponseDTO<MateBoardDTO> responseDTO = mateService.getList(matePageRequestDTO);
//        model.addAttribute("responseDTO", responseDTO);
//        return "mate/list";
//    }

    @GetMapping("/view")
    public void view(Long bno, Integer mode, Model model) {
        if (mode == null) mode = 1;
        model.addAttribute("board", mateService.findMateBoardById(bno, mode));
    }

    @GetMapping("register")
    public void registerGet(Model model) {
        log.info("registerGet");
    }

    //해결필요
    @PostMapping("register")
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
                log.info("originalFileName" + originalFileName); //파일 이름을 찍어줘. 1개든 2개든~
//                서버에 많은 사람이 쓰니까 같은 파일명을 올리면 난리가 나 uuid를 붙혀줘
                String uuid = UUID.randomUUID().toString(); //오리지날 파일 이름이 같은게 많으면 uuid 16자리,랜덤
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFileName); //file path를 선택하고 uu+진명
                boolean image = false; //uploadPath, , 콤마가 업로드 패스에 슬러시를 만들어주는데 + 를 쓰면 슬러시 넣어줘야해.
                try { //파일이 이미지라면 try 돌아
                    multifile.transferTo(savePath); // 파일이 저장됨 / multifile 파일의 전체정보 . 세이브 페스로 전송한다.
                    if (Files.probeContentType(savePath).startsWith("image")) { //probes에 image라고 붙어있으면 true
                        image = true; //savePath가 가진 속성컨텐츠 타입이 image면 이미지 파일이고 true로 하고 아래대로 섬네일 만들어.
                        File thumbnail = new File(uploadPath, "s_" + uuid + "_" + originalFileName); //섬네일 객체 하나 만들고
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200); //크기가 200 200
                    } //savePath에 있는 것을 toFile 하고 섬네일 만들고, 이미지 크기 설정 , 고려사항 실제 그림의 비율을 맞춰야해 큰거(ex, 가로)만 고정줘도 수정됨.
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                리스트에 넣기
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
                removed = resource.getFile().delete(); // 원본 파일 삭제, 이미지 파일이면 현재 리소스 파일이지워진다! 근데 섬네일 지워져 원본은 if로 해결
                if (contentType.startsWith("image")) {
                    String fileName1 = "s_" + filename; //섬네일의 s지우고
                    File thumFile = new File(uploadPath + File.separator + fileName1);
                    thumFile.delete(); //원본 파일이 지워져
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


//    @GetMapping("/register")
//    public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
//        MemberDTO memberDTO = null;
//        if (principal != null)
//            Member member = principal.getMember();
//        memberDTO = userService.getMemberByUsername(member.getUsername());
//        log.info("로그인한 회원 정보: {}", memberDTO);
//         }else{
//            log.info("비로그인 상태에서 글쓰기 페이지 접근");
//        }
//        model.addAttribute("memberDTO", memberDTO);
//    }
//
//    @PostMapping("/register")
//    public String registerPost(MateBoardDTO mateBoardDTO, MateUploadFileDTO mateUploadFileDTO, RedirectAttributes redirectAttributes) {
//        List<MateFileDTO> mateFileDTOS = null;
//        if(mateUploadFileDTO.getFiles() !== null && ! mateUploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
//            mateFileDTOS = fileUpload(mateUploadFileDTO);
//        }
//        mateBoardDTO.setMateFileDTOs(mateFileDTOS);
//        mateService.registerMateBoard(mateBoardDTO);
//        return "redirect:/mate/list";
//    }

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

    //    @ResponseBody
//    @GetMapping("/like")
//    public MateBoardDTO likePost(Long bno){
//        return mateService.updateLikecount(bno);
//    }

    @GetMapping("/like-test")
    @ResponseBody
    public String testLike(Long bno) {
        int updated = mateService.increaseLikeCountAndGet(bno);
        return "업데이트 후 likecount=" + updated;
    }


    @PostMapping(value = "/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> like(@RequestBody Map<String, Long> payload) {
        Long bno = payload.get("bno");
        int updated = mateService.increaseLikeCountAndGet(bno);
        return Map.of("likecount", updated);
    }


}






