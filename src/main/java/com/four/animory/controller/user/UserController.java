package com.four.animory.controller.user;

import com.four.animory.dto.user.UserRegisterDTO;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    log.info(userRegisterDTO.getMember());
    log.info(userRegisterDTO.getPets());

    int result = userService.register(userRegisterDTO);
    if (result == 1){
      redirectAttributes.addFlashAttribute("joinResult", "성공");
      return "redirect:/member/login";
    } else {
      redirectAttributes.addFlashAttribute("joinResult", "실패");
      return "redirect:/member/join";
    }
  }

}
