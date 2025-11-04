package com.four.animory.controller.notice;

import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// User Notice
@Controller
@Log4j2
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping({"", "/"})
    public String root() {
        return "redirect:/notice/list";
    }


    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("Notice List 요청");
        var responseDTO = noticeService.getList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("list", responseDTO.getDtoList());
        return "notice/list";
    }


    @GetMapping("/view")
    public String view(Long bno, Model model) {
        log.info("Notice View 요청 bno={}", bno);
        model.addAttribute("notice", noticeService.findNoticeById(bno, 1));
        return "notice/view";
    }
}
