package com.four.animory.controller.sitter;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
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

@Controller
@Log4j2
@RequestMapping("/sitter")
public class SitterController {
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
      if (principal != null) { // 로그인한 사용자가 있는 경우
          Member member = principal.getMember();
          MemberDTO memberDTO = userService.getMemberByUsername(member.getUsername());
          log.info("로그인한 회원 정보: {}", memberDTO);
          model.addAttribute("memberDTO", memberDTO);
      } else {
          log.info("비로그인 상태에서 글쓰기 페이지 접근");
      }
  }

  @PostMapping("/register")
  public String registerPost(@AuthenticationPrincipal PrincipalDetails principal, SitterBoardDTO sitterBoardDTO) {
    if(sitterBoardDTO.getCategory().equals("구해요")){
        sitterBoardDTO.setState("구인중");
    } else if (sitterBoardDTO.getCategory().equals("일해요")){
        sitterBoardDTO.setState("구직중");
    }
    sitterService.insertSitterBoard(sitterBoardDTO, principal.getMember());
    return "redirect:/sitter/list";
  }

  @GetMapping("/view")
  public void viewGet(@RequestParam("bno") Long bno,  Model model) {
    SitterBoardDTO boardDTO = sitterService.getSitterBoardById(bno);
    String username = boardDTO.getUsername();
    model.addAttribute("board", boardDTO);
    model.addAttribute("username", username);
  }
}
