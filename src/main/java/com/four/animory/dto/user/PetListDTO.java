package com.four.animory.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetListDTO {
  private Long mid;
  @Builder.Default
  private List<PetDTO> petDTO = new ArrayList<>();
}
