package com.four.animory.dto.spr;

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
public class SprReplyDTO {
    private Long rno;
    @NotEmpty
    private String content;
    private Long bno;
    private String author;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private boolean secret;
    private boolean deleted;
}
