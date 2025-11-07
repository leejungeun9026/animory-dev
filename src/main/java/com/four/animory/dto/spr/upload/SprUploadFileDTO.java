package com.four.animory.dto.spr.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SprUploadFileDTO {
    private List<MultipartFile> files;
}
