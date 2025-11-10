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
    private int replyCount;
    private int likecount;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<MateFileDTO> mateFileDTOs;
    private boolean complete;
    @Future
    private String dueDate;


}





