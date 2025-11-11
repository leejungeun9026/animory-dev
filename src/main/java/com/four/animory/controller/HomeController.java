package com.four.animory.controller;

import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.spr.SprBoardDTO;
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
  private SitterBoardService sitterBoardService;
  @Autowired
  private SprBoardService sprBoardService;


  @GetMapping({"/", "/index"})
  public String index(Model model){
    List<SprBoardDTO> sprBoard = sprBoardService.getTop10SprBoards();
    List<SitterBoardListDTO> sitterBoard = sitterBoardService.getRecent(4);

    model.addAttribute("sprBoard", sprBoard);
    model.addAttribute("sitterBoard", sitterBoard);
    return "index";
  }
}
