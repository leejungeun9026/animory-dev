package com.four.animory.dto.mate;

import com.four.animory.domain.mate.MateFile;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MateBoardDTO {
    private Long bno;
    @NotEmpty
    private String state;
    @NotEmpty
    private String category;
    private String perInfo;
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
    private int readCount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<MateFile> fileDTOs;


}
