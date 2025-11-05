package com.four.animory.controller.spr;

import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprReplyDTO;
import com.four.animory.service.spr.SprReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/replies")
public class SprReplyController {
    @Autowired
    private SprReplyService sprReplyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@RequestBody SprReplyDTO sprReplyDTO){
        log.info(sprReplyDTO);
        Map<String,Long> map = new HashMap<>();
        Long rno = sprReplyService.register( sprReplyDTO );
        map.put("rno",rno);
        return map;
    }
    @GetMapping("/{rno}")
    public SprReplyDTO view(@PathVariable("rno") Long rno){
        log.info(rno);
        SprReplyDTO sprReplyDTO = sprReplyService.read(rno);
        return sprReplyDTO;
    }
    @GetMapping("/list/{bno}")
    public PageResponseDTO<SprReplyDTO> getReplies(
            @PathVariable("bno") Long bno, PageRequestDTO  pageRequestDTO){
        log.info(pageRequestDTO);
        PageResponseDTO<SprReplyDTO> responseDTO = sprReplyService.getListOfBoard(bno,pageRequestDTO);
        return responseDTO;
    }
    @PostMapping("/delete/{rno}")
    public ResponseEntity<String> deleteReply(@PathVariable("rno") Long rno, @AuthenticationPrincipal UserDetails userDetails){
        sprReplyService.remove(rno, userDetails.getUsername());
        return ResponseEntity.ok("삭제되었습니다.");
    }
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(
        @PathVariable("rno") Long rno, @RequestBody SprReplyDTO sprReplyDTO
    ){
        sprReplyDTO.setRno(rno);
        sprReplyService.modify(sprReplyDTO);
        Map<String,Long> map = new HashMap<>();
        map.put("rno",rno);
        return map;
    }
}
