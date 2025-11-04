package com.four.animory.controller.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Log4j2
public class AdminController {
    @GetMapping({"", "/login"})
    public String admin(){
        return "/admin/login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(){
        return "redirect:/admin/notice/list";
    }

}
