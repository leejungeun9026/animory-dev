package com.four.animory.dto.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    private boolean owner;
    private boolean admin;


}
