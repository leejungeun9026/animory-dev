package com.four.animory.dto.sitter;

import com.four.animory.domain.sitter.SitterFile;
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
public class SitterBoardDTO {
  private Long bno;
  private String state;
  @NotEmpty
  private String category;
  private String petInfo;
  @NotEmpty
  private String sido;
  @NotEmpty
  private String sigungu;
  @NotEmpty
  private String title;
  @NotEmpty
  private String content;
  private String nickname;
  private String username;
  private int readCount;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private List<SitterFileDTO> fileDTOs;
}
