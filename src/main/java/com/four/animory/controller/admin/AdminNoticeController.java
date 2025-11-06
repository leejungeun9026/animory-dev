package com.four.animory.controller.admin;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;


    @GetMapping("/register")
    public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        if (principal != null) {
            Member member = principal.getMember();
            log.info("관리자 로그인 정보: {}", member.getNickname());
            model.addAttribute("nickname", member.getNickname());
        } else {
            log.info("비로그인 상태에서 관리자 공지 등록 페이지 접근");
        }

    }


    @PostMapping("/register")
    public String registerPost(@AuthenticationPrincipal PrincipalDetails principal,
                               NoticeBoardDTO dto) {
        if (principal != null) {
            dto.setNickname(principal.getMember().getNickname()); // 작성자 닉네임 세팅
        }
        noticeService.registerNotice(dto);
        return "redirect:/admin/notice/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        var responseDTO = noticeService.getList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("list", responseDTO.getDtoList());

    }


    @GetMapping("/view")
    public String view(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 1));
        return "admin/notice/view";
    }


    @GetMapping("/modify")
    public String modifyForm(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 0));
        return "admin/notice/modify";
    }


    @PostMapping("/modify")
    public String modifyPost(NoticeBoardDTO noticeBoardDTO, RedirectAttributes redirectAttributes) {
        noticeService.updateNotice(noticeBoardDTO);
        redirectAttributes.addAttribute("bno", noticeBoardDTO.getBno());
        redirectAttributes.addAttribute("mode", 1);
        return "redirect:/admin/notice/view";
    }


    @GetMapping("/remove")
    public String removeGet(@RequestParam Long bno) {
        noticeService.removeNotice(bno);
        return "redirect:/admin/notice/list";
    }
}
