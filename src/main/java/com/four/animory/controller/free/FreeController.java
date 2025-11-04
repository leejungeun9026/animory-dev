package com.four.animory.controller.free;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.service.free.FreeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/free")
public class FreeController {
    @Autowired
    private FreeService freeService;

    @GetMapping("/list")
    public String list(Model model) {
        List<FreeBoardDTO> freeboardList = freeService.findAllFreeBoards();
        model.addAttribute("freeboardList", freeboardList);
        return "free/list";
    }

    @GetMapping("/view/{bno}")
    public String view(@PathVariable Long bno, Model model) {
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno);
        model.addAttribute("freeBoard", freeBoardDTO);
        return "free/view";
    }

    @GetMapping("/register")
    public String register(){
        return "free/register";
    }

    @PostMapping("/register")
    public String register(FreeBoardDTO freeBoardDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        Member member = principal.getMember();

        freeBoardDTO.setWriter(member.getNickname());
        freeBoardDTO.setMno(member.getId());

        freeService.registerFreeBoard(freeBoardDTO);
        return "redirect:/free/list";
    }

    @GetMapping("/modify")
    public String modify(Long bno, Model model) {
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno);
        model.addAttribute("freeBoard", freeBoardDTO);
        return "free/modify";
    }

    @PostMapping("/modify")
    public String modify(FreeBoardDTO freeBoardDTO) {
        freeService.updateFreeBoard(freeBoardDTO);
        return "redirect:/free/view/" + freeBoardDTO.getBno() ;
    }

    @GetMapping("remove")
    public String removeFreeBoard(Long bno) {
        log.info("removeFreeBoard");
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno);
        freeService.deleteFreeBoardById(bno);
        return "redirect:/free/list";
    }
}
