package com.four.animory.dto.mate;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
public class MateReplyCountDTO {

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


    //추가
    public boolean getExpired() {
        if (dueDate == null || dueDate.isBlank()) return false;
        try {
            LocalDateTime due = LocalDateTime.parse(dueDate);
            return LocalDateTime.now().isAfter(due);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // 표시 전용: 화면에 뿌릴 문자열
    public String getDueDisplay() {
        return (dueDate == null) ? "" : dueDate.replace('T', ' ');
    }


}
