package com.four.animory.dto.mate;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MateReplyCountDTO {
//    private Long bno;
//    private String title;
//    private String nickname;
//    private int readcount;
//    private LocalDateTime regDate;
//    private Long replyCount;



    private Long bno;
    private String category;
    private String sido;
    private String sigungu;
    private String title;
    private String content;
    private String nickname;
    private String username;
    private int readCount;
    private Long replyCount;
    private int likecount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<MateFileDTO> mateFileDTOs;
    private boolean complete;
    @Future
    private String dueDate;



}
