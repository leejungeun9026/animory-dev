package com.four.animory.dto.sitter;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SitterReplyDTO {
  private Long rno;
  @NotEmpty
  private Long bno;
  @NotEmpty
  private String content;
  private String nickname;
  @NotEmpty
  private String username;
  private String userTel;
  private Long mid;
  private boolean isHiring;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime regDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateDate;
}
