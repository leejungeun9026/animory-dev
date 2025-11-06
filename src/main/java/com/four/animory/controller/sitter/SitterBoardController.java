package com.four.animory.controller.sitter;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import com.four.animory.dto.sitter.SitterBoardPageResponseDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.sitter.SitterService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/sitter")
public class SitterBoardController {
//    @Value("${com.four.animory.upload.path}")
//    private String uploadRootPath;

  @Autowired
  private SitterService sitterService;

  @Autowired
  private UserService userService;


  @GetMapping("/list")
  public void list(SitterBoardPageRequestDTO sitterBoardPageRequestDTO, Model model) {
    SitterBoardPageResponseDTO<SitterBoardListDTO> pageResponseDTO = sitterService.getSitterBoardListSearchPage(sitterBoardPageRequestDTO);
    log.info(pageResponseDTO);
    model.addAttribute("pageResponseDTO", pageResponseDTO);
  }

  @GetMapping("/register")
  public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
    MemberDTO memberDTO = userService.getMemberByUsername(principal.getMember().getUsername());
    model.addAttribute("memberDTO", memberDTO);
  }

  @PostMapping("/register")
  public String registerPost(@AuthenticationPrincipal PrincipalDetails principal, SitterBoardDTO sitterBoardDTO) {
    MemberDTO memberDTO = userService.getMemberByUsername(principal.getUsername());
    sitterService.insertSitterBoard(sitterBoardDTO, memberDTO);
    return "redirect:/sitter/list";
  }

  @GetMapping({"/view", "/modify"})
  public String viewAndModify(@RequestParam("bno") Long bno, @RequestParam("mode") String mode, SitterBoardPageRequestDTO sitterBoardPageRequestDTO, Model model) {
    SitterBoardDTO boardDTO = sitterService.getSitterBoardById(bno, mode);
    model.addAttribute("board", boardDTO);
    if(mode.equals("1") || mode.equals("0")) {
      return "/sitter/view";
    } else {
      return "/sitter/modify";
    }
  }

  @PostMapping("/modify")
  public String modify(@AuthenticationPrincipal PrincipalDetails principal, SitterBoardDTO sitterBoardDTO){
    String boardUsername = sitterBoardDTO.getUsername();
    String loginUsername = principal.getMember().getUsername();
    // 글 작성자와 로그인한 유저 일치여부 확인
    if(boardUsername.equals(loginUsername)) {
      sitterService.updateBoard(sitterBoardDTO);
    }
    Long bno = sitterBoardDTO.getBno();
    return "redirect:/sitter/view?bno=" + bno + "&mode=0";
  }

  @GetMapping("/remove")
  public String remove(@AuthenticationPrincipal PrincipalDetails principal, @RequestParam("bno") Long bno, RedirectAttributes redirectAttributes){
    String boardUsername = sitterService.getSitterBoardById(bno, "0").getUsername();
    String loginUsername = principal.getMember().getUsername();
    boolean flag = false;
    // 글 작성자와 로그인한 유저 일치여부 확인
    if(boardUsername.equals(loginUsername)) {
      int result = sitterService.deleteBoard(bno);
      if (result == 1) {
        flag = true;
        redirectAttributes.addFlashAttribute("deleteResult", "success");
      } else {
        flag = false;
        redirectAttributes.addFlashAttribute("deleteResult", "fail");
      }
    }
    if (flag){
      return "redirect:/sitter/list";
    }
    return "redirect:/sitter/view?bno=" + bno + "&mode=0";
  }
}
