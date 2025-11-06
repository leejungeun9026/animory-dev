package com.four.animory.dto.free.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultDTO {
    private String uuid;
    private String fileName;
    private String fileSize;
    private boolean image; // 이미지 인가 아닌가 T/F

    public String getLink(){ // 업로드 된 파일의 결과 가져오기
        if(image){
            return "s_"+uuid+"_"+fileName; // 이미지는 썸네일 파일의 이름 가져옴.
        }else {
            return uuid+"_"+fileName; // 일반 파일의 이름 가져옴.
        }
    }
}
