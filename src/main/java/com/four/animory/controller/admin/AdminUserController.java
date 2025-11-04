package com.four.animory.controller.admin;

import com.four.animory.dto.region.SidoDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.dto.user.MemberListPetCountDTO;
import com.four.animory.dto.user.PetDTO;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/member")
@Log4j2
public class AdminUserController {
  @Autowired
  private UserService userService;


  @GetMapping("/list")
  public void list(Model model) {
    log.info("admin member list success.....");
    List<MemberListPetCountDTO> list = userService.getMemberListPetCount();
    model.addAttribute("memberList", list);
  }

  @GetMapping("/petlist/{mid}")
  @ResponseBody
  public List<PetDTO> getPetList (@PathVariable("mid") Long mid) {
    return userService.getPetListByMemberId(mid);
  }

  @GetMapping("/sitter/{mid}")
  @ResponseBody
  public boolean getSitter (@PathVariable("mid") Long mid) {
    return userService.getSitterById(mid);
  }

  @PutMapping(value = "/sitter/{mid}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Map<String, Object> modifySitter(@PathVariable Long mid, @RequestBody MemberDTO memberDTO) {
    memberDTO.setMid(mid);
    memberDTO.setSitter(memberDTO.isSitter());
    userService.modifySitter(memberDTO);

    Map<String, Object> response = new HashMap<>();
    response.put("mid", mid);
    response.put("sitter", memberDTO.isSitter());
    return response;
  }
}
