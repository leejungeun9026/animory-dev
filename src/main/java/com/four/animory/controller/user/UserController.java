package com.four.animory.controller.user;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.dto.user.PetDTO;
import com.four.animory.dto.user.PetRequestDTO;
import com.four.animory.dto.user.UserRegisterDTO;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/member")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/login")
  public void loginGet() {
  }

  @GetMapping("/join")
  public void joinGet() {
  }

  @PostMapping("/joinRegister")
  public String joinPost(UserRegisterDTO userRegisterDTO, RedirectAttributes redirectAttributes) {
    int result = userService.register(userRegisterDTO);
    if (result == 1){
      redirectAttributes.addFlashAttribute("joinResult", "성공");
      return "redirect:/member/login";
    } else {
      redirectAttributes.addFlashAttribute("joinResult", "실패");
      return "redirect:/member/join";
    }
  }

  @GetMapping("/mypage")
  public String mypageGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
    if (principal == null) return "redirect:/member/login";
    Member member = principal.getMember();
    MemberDTO memberDTO = userService.getMemberByUsername(member.getUsername());
    List<PetDTO> petDTOs = userService.getPetListByMemberId(member.getId());
    model.addAttribute("memberDTO", memberDTO);
    model.addAttribute("petDTOs", petDTOs);
    return "member/mypage";
  }

  @GetMapping("/modifyPet")
  public void modifyGet(@RequestParam("mid") Long mid, Model model) {
    model.addAttribute("petDTOs", userService.getPetListByMemberId(mid));
  }

  @PostMapping("/modifyPet")
  public String modifyPost(PetRequestDTO petRequestDTO, Model model, RedirectAttributes redirectAttributes) {
    for (PetDTO petDTO : petRequestDTO.getPets()) {
      log.info(petDTO);
    }
    return "redirect:/index";
  }
}
