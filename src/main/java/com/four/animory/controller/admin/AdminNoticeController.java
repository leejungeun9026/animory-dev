package com.four.animory.controller.admin;

import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/admin/notice")
public class AdminNoticeController {
    private final NoticeService noticeService;
    public AdminNoticeController(NoticeService noticeService){ this.noticeService = noticeService; }

    @GetMapping("/register")
    public void registerGet(){}

    @PostMapping("/register")
    public String registerPost(NoticeBoardDTO noticeBoardDTO){
        log.info("registerPost");
        Long bno = noticeService.registerNotice(noticeBoardDTO);
        log.info("bno = " + bno);
        return "redirect:/admin/notice/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        var responseDTO = noticeService.getList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("list", responseDTO.getDtoList());
    }

    @GetMapping("/view")
    public String view(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 1)); // 조회수 +
        return "admin/notice/view";
    }

    @GetMapping("/modify")
    public String modifyForm(@RequestParam Long bno, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(bno, 0)); // 조회수 X
        return "admin/notice/modify";
    }


    @PostMapping("/modify")
    public String modifyPost(NoticeBoardDTO noticeBoardDTO, RedirectAttributes redirectAttributes){
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