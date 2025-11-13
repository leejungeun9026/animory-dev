package com.four.animory.controller;

import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.service.free.FreeService;
import com.four.animory.service.mate.MateService;
import com.four.animory.service.notice.NoticeService;
import com.four.animory.service.sitter.SitterBoardService;
import com.four.animory.service.spr.SprBoardService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
public class HomeController {
  @Autowired
  private FreeService freeBoardService;
  @Autowired
  private MateService mateBoardService;
  @Autowired
  private SprBoardService sprBoardService;
  @Autowired
  private SitterBoardService sitterBoardService;
    @Autowired
    private NoticeService noticeService;


    @GetMapping({"/", "/index"})
  public String index(Model model){
    List<FreeBoardDTO> freeBoard = freeBoardService.getTop10FreeBoardList();
    List<MateBoardDTO> mateBoard = mateBoardService.getTop10MateBoardList();
    List<SprBoardDTO> sprBoard = sprBoardService.getTop10SprBoards();
    List<NoticeBoardDTO>noticeBoard = noticeService.getTop10NoticeBoardList();
    List<SitterBoardListDTO> sitterBoard = sitterBoardService.getRecent(4);
    log.info(mateBoard);
    model.addAttribute("freeBoard", freeBoard);
    model.addAttribute("sprBoard", sprBoard);
    model.addAttribute("noticeBoard", noticeBoard);
    model.addAttribute("sitterBoard", sitterBoard);
    return "index";
  }

  @GetMapping("/maptest")
  public void test() {

  }
}
