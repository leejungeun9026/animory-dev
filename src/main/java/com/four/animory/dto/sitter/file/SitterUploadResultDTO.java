package com.four.animory.dto.sitter.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitterUploadResultDTO {
  private String uuid;
  private String filename;
  private boolean image;

  public String getLink(){
    if (image) {
      return "s+" + uuid + "_" + filename;
    } else {
      return uuid + "_" + filename;
    }
  }
}
