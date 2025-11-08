package com.four.animory.dto.mate.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MateUploadResultDTO {
    private String uuid;
    private String fileName;
    private boolean image;

    public String getLink(){
        if(image){
            return "s_"+uuid+"_"+fileName;
        }else{
            return uuid+"_"+fileName; //이미지가 아니라면 uuid만 붙혀서 리턴함.
        }
    }



}
