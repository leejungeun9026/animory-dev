package com.four.animory.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
  private MemberDTO member;
  @Builder.Default
  private List<PetDTO> pets = new ArrayList<>();
}
