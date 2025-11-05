package com.four.animory.controller.free;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.free.FreeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/view")
    public void view(Long bno, Integer mode, Model model) {
        if(mode == null) mode = 1;
        model.addAttribute("freeBoard", freeService.findFreeBoardById(bno,mode));
    }


    @GetMapping("/register")
    public String registerGet(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);
        return "free/register"; // templates/free/register.html
    }

    @PostMapping("/register")
    public String registerPost(FreeBoardDTO freeBoardDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        log.info(member);
        freeService.registerFreeBoard(freeBoardDTO, member); // 서비스에는 DTO만 전달
        return "redirect:/free/list";
    }

    @GetMapping("/modify")
    public String modify(Long bno, Integer mode, Model model) {
        log.info("모드 확인:" +mode);
        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno,mode);
        model.addAttribute("freeBoard", freeBoardDTO);
        return "free/modify";
    }

    @PostMapping("/modify")
    public String modify(FreeBoardDTO freeBoardDTO) {
        freeService.updateFreeBoard(freeBoardDTO);
        return "redirect:/free/view?bno=" + freeBoardDTO.getBno();
    }

//    @GetMapping("remove")
//    public String removeFreeBoard(Long bno, Integer mode) {
//        log.info("removeFreeBoard");
//        FreeBoardDTO freeBoardDTO = freeService.findFreeBoardById(bno,mode);
//        freeService.deleteFreeBoardById(bno);
//        return "redirect:/free/list";
//    }

    @GetMapping("remove")
    public String removeFreeBoard(Long bno) {
        freeService.deleteFreeBoardById(bno);
        return "redirect:/free/list";
    }
}
