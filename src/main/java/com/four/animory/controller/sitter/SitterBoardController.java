package com.four.animory.controller.sitter;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.sitter.SitterFile;
import com.four.animory.dto.sitter.*;
import com.four.animory.dto.sitter.file.SitterUploadFileDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.dto.user.MemberWithPetCountDTO;
import com.four.animory.service.sitter.SitterBoardService;
import com.four.animory.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/sitter")
public class SitterBoardController {
    @Value("${com.four.animory.upload.path}")
    private String uploadRootPath;

  @Autowired
  private SitterBoardService sitterBoardService;
  @Autowired
  private UserService userService;


  @GetMapping("/list")
  public void list(SitterBoardPageRequestDTO sitterBoardPageRequestDTO, Model model) {
    SitterBoardPageResponseDTO<SitterBoardListDTO> pageResponseDTO = sitterBoardService.getSitterBoardListSearchPage(sitterBoardPageRequestDTO);
    log.info(pageResponseDTO);
    model.addAttribute("pageResponseDTO", pageResponseDTO);
  }

  @GetMapping("/register")
  public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
    MemberDTO memberDTO = userService.getMemberByUsername(principal.getMember().getUsername());
    model.addAttribute("memberDTO", memberDTO);
  }

  @PostMapping("/register")
  public String registerPost(@AuthenticationPrincipal PrincipalDetails principal, @Valid SitterBoardDTO sitterBoardDTO, SitterUploadFileDTO sitterUploadFileDTO) {
    List<SitterFileDTO> sitterFileDTO = null;
    if(sitterUploadFileDTO.getFiles() != null && !sitterUploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
      sitterFileDTO = uploadFile(sitterUploadFileDTO);
    }
    sitterBoardDTO.setFileDTOs(sitterFileDTO);

    MemberDTO memberDTO = userService.getMemberByUsername(principal.getUsername());
    sitterBoardService.insertSitterBoard(sitterBoardDTO, memberDTO);
    return "redirect:/sitter/list";
  }

  @GetMapping({"/view", "/modify"})
  public String viewAndModify(@RequestParam("bno") Long bno, @RequestParam("mode") String mode, SitterBoardPageRequestDTO sitterBoardPageRequestDTO, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
    MemberWithPetCountDTO loginUser = null;
    if(principal != null) {
      MemberDTO memberDTO = userService.getMemberById(principal.getMember().getId());
      Long petCount = (long) userService.getPetListByMemberId(principal.getMember().getId()).size();
      loginUser = new MemberWithPetCountDTO(memberDTO, petCount);
    }
    log.info(loginUser);
    SitterBoardDTO boardDTO = sitterBoardService.getSitterBoardById(bno, mode);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("board", boardDTO);
    if(mode.equals("1") || mode.equals("0")) {
      return "/sitter/view";
    } else {
      return "/sitter/modify";
    }
  }

  @PostMapping("/modify")
  public String modify(SitterBoardDTO sitterBoardDTO, SitterUploadFileDTO sitterUploadFileDTO){
    List<SitterFileDTO> sitterFileDTO = null;
    if(sitterUploadFileDTO.getFiles() != null && !sitterUploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
      // 기존 파일 가져와서 삭제
      SitterBoardDTO oldDTO = sitterBoardService.getSitterBoardById(sitterBoardDTO.getBno(), "0");
      List<SitterFileDTO> oldDTOList = oldDTO.getFileDTOs();
      if(oldDTOList != null && !oldDTOList.isEmpty()) {
        removeFile(oldDTOList);
      }
      // 새로운 파일 업로드
      sitterFileDTO = uploadFile(sitterUploadFileDTO);
    }
    sitterBoardDTO.setFileDTOs(sitterFileDTO);
    sitterBoardService.updateBoard(sitterBoardDTO);
    Long bno = sitterBoardDTO.getBno();
    return "redirect:/sitter/view?bno=" + bno + "&mode=0";
  }

  @GetMapping("/modify/{bno}")
  public String modifyState(@PathVariable("bno") Long bno) {
    sitterBoardService.updateState(bno);
    return "redirect:/sitter/view?bno=" + bno + "&mode=0";
  }

  @GetMapping("/remove")
  public String remove(@RequestParam("bno") Long bno, RedirectAttributes redirectAttributes){
    int result = sitterBoardService.deleteBoard(bno);
    if (result == 1) {
      redirectAttributes.addFlashAttribute("deleteResult", "success");
      return "redirect:/sitter/list";
    } else {
      redirectAttributes.addFlashAttribute("deleteResult", "fail");
    return "redirect:/sitter/view?bno=" + bno + "&mode=0";
    }
  }


  private List<SitterFileDTO> uploadFile(SitterUploadFileDTO uploadFileDTO) {
    String uploadPath = uploadRootPath + "\\sitter";
    List<SitterFileDTO> list = new ArrayList<>();

    if(uploadFileDTO.getFiles() != null) {
      uploadFileDTO.getFiles().forEach(multipartFile -> {
        String originalName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

        boolean image = false;
        try{
          multipartFile.transferTo(savePath);

          if(Files.probeContentType(savePath).startsWith("image")) {
            image = true;
            File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
          }
        } catch(IOException e) {
          e.printStackTrace();
        }
        SitterFileDTO sitterFileDTO = SitterFileDTO.builder()
            .uuid(uuid)
            .filename(originalName)
            .image(image)
            .build();
        list.add(sitterFileDTO);
      });
    }
    return list;
  }

  public void removeFile(List<SitterFileDTO> sitterFileDTOs) {
    String uploadPath = uploadRootPath + "\\sitter";

    for(SitterFileDTO dtos: sitterFileDTOs){
      String filename = dtos.getUuid()+"_"+dtos.getFilename();
      Resource resource = new FileSystemResource(uploadPath+File.separator+filename);

      String resourceName = resource.getFilename();
      boolean removed = false;
      try {
        String contentType = Files.probeContentType(resource.getFile().toPath());
        removed = resource.getFile().delete();
        if(contentType.startsWith("image")){
          // 썸네일이 있다면 s_삭제하고 오리지날 파일도 지움
          File thumbnailFile = new File(uploadPath + File.separator + "s_" + filename);
          thumbnailFile.delete();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


}
