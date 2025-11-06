package com.four.animory.dto.free.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO { // "파일만 받기"가 목적인 DTO
    private List<MultipartFile> files; // 업로드 되는 파일의 모든 정보를 다 받아줌.
}
