package com.four.animory.controller.notice;

import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeReplyDTO;
import com.four.animory.service.notice.NoticeReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/noticereplies")
public class NoticeReplyController {

    private final NoticeReplyService noticeReplyService;

    // 등록
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@RequestBody NoticeReplyDTO dto,
                                      @AuthenticationPrincipal UserDetails user) {
        log.info("notice reply register: {}", dto);
        String username = user.getUsername();
        Long rno = noticeReplyService.register(dto, username);
        Map<String, Long> res = new HashMap<>();
        res.put("rno", rno);
        return res;
    }

    // 단건 조회
    @GetMapping("/{rno}")
    public NoticeReplyDTO view(@PathVariable("rno") Long rno){
        log.info("notice reply view rno={}", rno);
        return noticeReplyService.read(rno);
    }

    // 게시글별 목록 + 페이징
    @GetMapping("/list/{bno}")
    public PageResponseDTO<NoticeReplyDTO> list(@PathVariable("bno") Long bno,
                                                PageRequestDTO pageRequestDTO){
        log.info("notice reply list bno={}, {}", bno, pageRequestDTO);
        return noticeReplyService.getListOfBoard(bno, pageRequestDTO);
    }

    // 삭제(작성자 또는 ADMIN)
    @PostMapping("/delete/{rno}")
    public ResponseEntity<String> delete(@PathVariable("rno") Long rno,
                                         @AuthenticationPrincipal UserDetails user){
        noticeReplyService.remove(rno, user.getUsername());
        return ResponseEntity.ok("삭제되었습니다.");
    }

    // 수정
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                    @RequestBody NoticeReplyDTO dto){
        dto.setRno(rno);
        noticeReplyService.modify(dto);
        Map<String, Long> res = new HashMap<>();
        res.put("rno", rno);
        return res;
    }
}
