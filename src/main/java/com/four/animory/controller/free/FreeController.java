package com.four.animory.controller.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.service.free.FreeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
