package com.four.animory.dto.free;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FreeBoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String username;
    private String nickname;
    private String btype;
    private int readcount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private Long replycount;
    private int likecount;
}
