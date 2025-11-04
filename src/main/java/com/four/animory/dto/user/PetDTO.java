package com.four.animory.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
  private Long pid;
  @NotEmpty
  private String type;
  @NotEmpty
  private String name;
  @NotEmpty
  private int age;
  private String owner;
}
