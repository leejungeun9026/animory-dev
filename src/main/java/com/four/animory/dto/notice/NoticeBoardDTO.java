package com.four.animory.dto.notice;

import com.four.animory.domain.notice.NoticeFile;
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
public class NoticeBoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private int readCount;
    private boolean isPinned;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<NoticeFile> fileDTOs;
    private String nickname;





}
