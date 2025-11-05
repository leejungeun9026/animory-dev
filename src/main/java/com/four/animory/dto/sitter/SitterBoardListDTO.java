package com.four.animory.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SitterBoardListDTO {
  private Long bno;
  private String state;
  private String category;
  private String petInfo;
  private String sido;
  private String sigungu;
  private String title;
  private String nickname;
  private int readCount;
//  private int replyCount;
  private LocalDateTime regDate;
}
