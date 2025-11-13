package com.four.animory.dto.free.upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FreeFileThumbnailDTO {
    private Long bno;
    private String thumbnailName;
    private String thumbnailUuid;
}
