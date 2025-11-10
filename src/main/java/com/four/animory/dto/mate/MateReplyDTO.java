package com.four.animory.dto.mate;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Data
public class MateReplyDTO {
    private Long rno;

    @NotEmpty
    private String content;
    private Long bno;
    private String nickname;
    private String username;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private boolean deleted;
    private boolean secret;
    private Long mid;






}
