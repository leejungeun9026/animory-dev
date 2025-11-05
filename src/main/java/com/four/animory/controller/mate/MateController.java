package com.four.animory.controller.mate;

import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.service.mate.MateService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/mate")
public class MateController {

    @Autowired
    private MateService mateService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<MateBoardDTO> mateBoards = mateService.findAllMateBoards();
        model.addAttribute("mateboards", mateBoards);
        return "mate/list";
    }








    @GetMapping("/register")
    public void registerGet(Model model) { log.info("registerGet"); }

    @PostMapping("/register")
    public String registerPost(MateBoardDTO mateboardDTO){
        log.info("registerPost 실행: {}", mateboardDTO);
        mateService.registerMateBoard(mateboardDTO);
        return "redirect:/mate/list";

    }






}
