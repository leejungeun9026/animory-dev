package com.four.animory.dto.free;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String content;
    private Long bno;
    private String username;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime regDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateDate;
    private boolean deleted;
    private Long mid;
    private Long parentRno;
    private String parentUsername;
    private String parentNickname;
}
