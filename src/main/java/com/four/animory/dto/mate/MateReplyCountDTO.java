package com.four.animory.dto.mate;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MateReplyCountDTO {
    private Long bno;
    private String title;
    private String nickname;
    private int readcount;
    private LocalDateTime regDate;
    private Long replyCount;
}
