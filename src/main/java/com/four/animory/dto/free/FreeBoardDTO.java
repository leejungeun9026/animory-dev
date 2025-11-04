package com.four.animory.dto.free;

import com.four.animory.domain.free.FreeFile;
import com.four.animory.domain.sitter.SitterFile;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String writer;
    private String btype;
    private int readcount;
    private int likecount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<FreeFile> fileDTOs;

    private int replycount;
}
