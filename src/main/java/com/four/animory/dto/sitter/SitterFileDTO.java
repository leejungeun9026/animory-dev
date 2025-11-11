package com.four.animory.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitterFileDTO {
  private String uuid;
  private String filename;
  private int ord;
  private boolean image;
}
