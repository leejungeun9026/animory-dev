package com.four.animory.controller.free;

import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.dto.free.FreePageResponseDTO;
import com.four.animory.dto.free.FreeReplyDTO;
import com.four.animory.service.free.FreeReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/freeReplies")
public class FreeReplyController {
    @Autowired
    private FreeReplyService freeReplyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> insertFreeReply(@RequestBody FreeReplyDTO freeReplyDTO, Authentication authentication) {
        log.info(freeReplyDTO.toString());
        String username = authentication.getName();
        Map<String, Long> map = new HashMap<>();
        Long rno = freeReplyService.insertFreeReply(freeReplyDTO, username);
        map.put("rno", rno);
        return map;
    }

    @GetMapping("/{rno}")
    public FreeReplyDTO readFreeReply(@PathVariable("rno") Long rno){
        log.info(rno.toString());
        FreeReplyDTO freeReplyDTO = freeReplyService.readFreeReply(rno);
        return freeReplyDTO;
    }

    @GetMapping("/list/{bno}")
    public FreePageResponseDTO<FreeReplyDTO> getReplies(@PathVariable("bno") Long bno, FreePageRequestDTO freePageRequestDTO) {
        log.info(freePageRequestDTO.toString());
        FreePageResponseDTO<FreeReplyDTO> responseDTO = freeReplyService.getListOfFreeBoard(bno, freePageRequestDTO);
        return responseDTO;
    }

    @PostMapping("/delete/{rno}")
    public ResponseEntity<String> deleteFreeReply(@PathVariable("rno") Long rno, @AuthenticationPrincipal UserDetails userDetails){
        String loginRole = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");
        freeReplyService.deleteFreeReply(rno, userDetails.getUsername(), loginRole);
        return  ResponseEntity.ok("삭제되었습니다.");
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(
            @PathVariable("rno") Long rno, @RequestBody FreeReplyDTO freeReplyDTO){
        freeReplyDTO.setRno(rno);
        freeReplyService.modifyFreeReply(freeReplyDTO);
        Map<String,Long> map = new HashMap<>();
        map.put("rno",rno);
        return map;
    }
}
