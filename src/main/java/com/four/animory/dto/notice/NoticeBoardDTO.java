package com.four.animory.dto.notice;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private int readCount;
    private boolean isPinned;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String author;
    //private List<NoticeFile> fileDTOs;




}
