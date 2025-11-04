package com.four.animory.controller.mate;

import com.four.animory.service.mate.MateService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/mate")
public class MateController {

    @Autowired
    private MateService mateService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public void list() {
    }
}
