package com.four.animory.dto.mate;

import com.four.animory.domain.mate.MateFile;
import com.four.animory.dto.spr.SprFileDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MateBoardDTO {
    private Long bno;
    @NotEmpty
    private String category;
    @NotEmpty
    private String sido;
    @NotEmpty
    private String sigungu;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
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

//    //추가
//    public boolean getExpired() {
//        if (dueDate == null || dueDate.isBlank()) return false;
//        try {
//            // "yyyy-MM-dd'T'HH:mm" or "yyyy-MM-dd'T'HH:mm:ss" 포맷 가정
//            LocalDateTime due = LocalDateTime.parse(dueDate);
//            return LocalDateTime.now().isAfter(due);
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }
//
//    public String getDueDisplay() {
//        return (dueDate == null) ? "" : dueDate.replace('T', ' ');
//    }



}





