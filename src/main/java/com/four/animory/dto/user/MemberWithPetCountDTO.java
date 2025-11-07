package com.four.animory.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberWithPetCountDTO {
  private Long mid;
  String username;
  String nickname;
  String tel;
  String email;
  String sido;
  String sigungu;
  boolean sitter;
  LocalDateTime regDate;
  LocalDateTime updateDate;
  Long petCount;

  public MemberWithPetCountDTO(MemberDTO memberDTO, Long Count) {
    this.mid = memberDTO.getMid();
    this.username = memberDTO.getUsername();
    this.nickname = memberDTO.getNickname();
    this.tel = memberDTO.getTel();
    this.email = memberDTO.getEmail();
    this.sido = memberDTO.getSido();
    this.sigungu = memberDTO.getSigungu();
    this.sitter = memberDTO.isSitter();
    this.regDate = memberDTO.getRegDate();
    this.updateDate = memberDTO.getUpdateDate();
    this.petCount = Count;
  }
}
