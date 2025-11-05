package com.four.animory.dto.free;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeReplyDTO {
    private Long rno;
    @NotEmpty
    private Long bno;
    @NotEmpty
    private String content;
    @NotEmpty
    private String username;
    @NotEmpty
    private String nickname;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
