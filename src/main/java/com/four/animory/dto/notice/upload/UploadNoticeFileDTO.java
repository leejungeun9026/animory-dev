package com.four.animory.dto.notice.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadNoticeFileDTO {
    private List<MultipartFile> files;
}
