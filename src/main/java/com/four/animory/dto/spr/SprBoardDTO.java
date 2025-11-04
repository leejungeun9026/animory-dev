package com.four.animory.dto.spr;

import com.four.animory.domain.sitter.SitterFile;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprBoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String category;
    @NotEmpty
    private String sido;
    @NotEmpty
    private String sigungu;
    @NotEmpty
    private String content;
    @NotEmpty
    private String nickname;
    private int readcount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<SitterFile> fileDTOs;
    @Future
    private String dueDate;
    private int recommend;
    private boolean complete;
    private Long replyCount;
    private String author;
}
