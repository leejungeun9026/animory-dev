package com.four.animory.dto.notice;

import com.four.animory.domain.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeReplyDTO {

    private Long rno;
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    private Long bno;
    private Long mno;
    private String nickname;
    private boolean deleted;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
