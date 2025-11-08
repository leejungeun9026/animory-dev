package com.four.animory.controller.sitter;

import com.four.animory.dto.sitter.SitterReplyDTO;
import com.four.animory.repository.sitter.SitterReplyRepository;
import com.four.animory.service.sitter.SitterReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/sitter/replies")
public class SitterReplyController {
  @Autowired
  private SitterReplyService sitterReplyService;

  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void register(@RequestBody SitterReplyDTO sitterReplyDTO) {
    log.info("register......" + sitterReplyDTO);
    sitterReplyService.insertReply(sitterReplyDTO);
  }

  @GetMapping("/list/{bno}")
  public List<SitterReplyDTO> getReplyList(@PathVariable("bno") Long bno) {
    return sitterReplyService.findAllByBno(bno);
  }

  @GetMapping("/{rno}")
  public SitterReplyDTO getReply(@PathVariable("rno") Long rno) {
    return sitterReplyService.getReply(rno);
  }

  @PutMapping(value = "/{rno}", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void modifyReply(@PathVariable("rno")Long rno, @RequestBody SitterReplyDTO sitterReplyDTO){
    log.info(sitterReplyDTO);
    sitterReplyService.updateReply(sitterReplyDTO);
  }

  @DeleteMapping("/{rno}")
  public void deleteReply(@PathVariable("rno")Long rno){
    sitterReplyService.deleteReply(rno);
  }
}
