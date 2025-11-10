package com.four.animory.dto.sitter.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SitterUploadFileDTO {
  private List<MultipartFile> files;
}
