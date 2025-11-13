package com.four.animory.dto.free;

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
    private String username;
    private String nickname;
    @NotEmpty
    private String btype;
    private int readcount;
    private int likecount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private int replycount;
    private Long mno; // 로그인 회원 ID용 필드

    private List<FreeFileDTO> freeFileDTOS;
//    private List<FreeFile> fileDTOs;

    private String thumbnailMain;

}
