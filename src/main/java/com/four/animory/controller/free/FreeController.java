package com.four.animory.controller.free;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.*;
import com.four.animory.dto.free.upload.FreeFileThumbnailDTO;
import com.four.animory.service.free.FreeService;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/free")
public class FreeController {
    @Value("${com.four.animory.upload.path}")
    private String uploadPath;

    @Autowired
    private FreeService freeService;

    //    @GetMapping("/list")
    public String list(Model model) {
        List<FreeBoardDTO> freeboardList = freeService.findAllFreeBoards();
        model.addAttribute("freeboardList", freeboardList);
        return "free/list";
    }

//    @GetMapping("/list")
//    public String replyCountList(FreePageRequestDTO freePageRequestDTO, Model model) {
//        List<FreeBoardDTO> freeboardList = freeService.findAllFreeBoards();
//        model.addAttribute("freeboardList", freeboardList);
//
//        // 게시글 썸네일 리스트
//        List<FreeFileThumbnailDTO> thumbnails = freeService.getBoardThumbnails();
//        model.addAttribute("thumbnails", thumbnails);
//
//        FreePageResponseDTO<FreeBoardListReplyCountDTO> freePageResponseDTO =
//        freeService.getListReplyCount(freePageRequestDTO);
//        model.addAttribute("freePageResponseDTO", freePageResponseDTO);
//        model.addAttribute("freePageRequestDTO",  freePageRequestDTO);
//        return "free/list";
//    }

    @GetMapping("/list")
    public String replyCountList(FreePageRequestDTO freePageRequestDTO, Model model) {
        // 기존 게시글 리스트
        List<FreeBoardDTO> freeboardList = freeService.findAllFreeBoards();
        model.addAttribute("freeboardList", freeboardList);
        // 게시글 썸네일 리스트
        List<FreeFileThumbnailDTO> thumbnails = freeService.getBoardThumbnails();
        // 댓글 수 포함 게시글 리스트
        FreePageResponseDTO<FreeBoardListReplyCountDTO> freePageResponseDTO =
                freeService.getListReplyCount(freePageRequestDTO);
        List<FreeBoardListReplyCountDTO> boardList = freePageResponseDTO.getDtoList();
        // bno 기준으로 DTO에 썸네일 매칭
        for (FreeBoardListReplyCountDTO board : boardList) {
            for (FreeFileThumbnailDTO thumb : thumbnails) {
                if (board.getBno().equals(thumb.getBno())) {
                    board.setThumbnailFilename(thumb.getThumbnailUuid() + "_" + thumb.getThumbnailName());
                    break;
                }
            }
        }
        model.addAttribute("freePageResponseDTO", freePageResponseDTO);
        model.addAttribute("freePageRequestDTO", freePageRequestDTO);
        return "free/list";
    }



        @GetMapping("/view")
    public void view(Long bno, Integer mode, Model model) {
        if (mode == null) mode = 1;
        model.addAttribute("freeBoard", freeService.findFreeBoardById(bno, mode));
    }

    @GetMapping("/register")
    public String registerGet(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);
        return "free/register"; // templates/free/register.html
    }

    @GetMapping("/modify")
    public String modify(Long bno, Integer mode, Model model) {
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno, mode);
        model.addAttribute("freeBoard", freeBoardDTO);
        return "free/modify";
    }

    @GetMapping("remove")
    public String removeFreeBoard(Long bno) {
        log.info("removeFreeBoard");
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno,2);
        List<FreeFileDTO> freeFileDTOS = freeBoardDTO.getFreeFileDTOS();
        if (freeFileDTOS != null && !freeFileDTOS.isEmpty()) {
            log.info("!!!!!!!!! removeFreeBoard");
            removeFile(freeFileDTOS);
        }
        freeService.deleteFreeBoardById(bno);
        return "redirect:/free/list";
    }

    public void removeFile(List<FreeFileDTO> freeFileDTO) { // 이미지일 경우에는 썸네일이 옴
        String freeUploadPath = uploadPath + "\\free";
        for(FreeFileDTO freeFileDTO1: freeFileDTO){
            String filename=freeFileDTO1.getUuid()+"_"+freeFileDTO1.getFilename();
            log.info("removeFile: "+filename);
            Resource resource = new FileSystemResource(freeUploadPath+File.separator+filename);
            String resourceName = resource.getFilename();
            boolean removed=false;

            try{
                String contentType=Files.probeContentType(resource.getFile().toPath());
                removed=resource.getFile().delete(); // 원본 파일 삭제

                if(contentType.startsWith("image")){
                    String fileName1="s_"+filename; // 썸네일 파일
                    File thumFile=new File(freeUploadPath+File.separator+fileName1); // 원본 파일로 형태로 만들어짐
                    thumFile.delete(); // 원본 파일이 삭제 됨.
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @GetMapping("/like")
    public FreeBoardDTO updatelikecount(Long bno) {
        return freeService.updateLikecount(bno);
    }


}