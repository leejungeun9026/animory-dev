package com.four.animory.dto.free;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FreeBoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String username;
    private int readcount;
    private LocalDateTime regDate;
    private Long replycount;
}
