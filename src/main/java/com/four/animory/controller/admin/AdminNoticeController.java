package com.four.animory.controller.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notice")
@Log4j2
public class AdminNoticeController {
  @GetMapping("/list")
  public void list(){
  }

  @GetMapping("/register")
  public void registerGet(){
  }

  @GetMapping("/view")
  public void view(){
  }
}
