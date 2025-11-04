package com.four.animory.dto.user;

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
public class MemberListPetCountDTO {
  private Long mid;
  String username;
  String nickname;
  String tel;
  String email;
  String sido;
  String sigungu;
  boolean sitter;
  LocalDateTime regDate;
  Long petCount;
}
