package com.four.animory.controller.mate;


import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.MateReplyDTO;
import com.four.animory.service.mate.MateReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/matereplies")
public class MateReplyController {
    @Autowired
    private MateReplyService mateReplyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@RequestBody MateReplyDTO mateReplyDTO, Authentication authentication) {
        log.info(mateReplyDTO);
        String username = authentication.getName();
        Map<String, Long> map = new HashMap<>();
        Long rno = mateReplyService.register(mateReplyDTO, username);
        map.put("rno", rno);
        return map;
    }

    @GetMapping("/{rno}")
    public MateReplyDTO view(@PathVariable("rno") Long rno){
        log.info(rno);
        MateReplyDTO mateReplyDTO = mateReplyService.read(rno);
        return mateReplyDTO;
    }

    @GetMapping("/list/{bno}")
    public PageResponseDTO<MateReplyDTO> getReplies(
            @PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
        log.info(pageRequestDTO);
        PageResponseDTO<MateReplyDTO> responseDTO = mateReplyService.getListOfBoard(bno,pageRequestDTO);
        return responseDTO;
    }

    @PostMapping("/delete/{rno}")
    public ResponseEntity<String> deleteReply(@PathVariable("rno") Long rno, @AuthenticationPrincipal UserDetails userDetails){
        mateReplyService.remove(rno, userDetails.getUsername());
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(
            @PathVariable("rno") Long rno, @RequestBody MateReplyDTO mateReplyDTO
    ){
        mateReplyDTO.setRno(rno);
        mateReplyService.modify(mateReplyDTO);
        Map<String,Long> map = new HashMap<>();
        map.put("rno",rno);
        return map;
    }



}

