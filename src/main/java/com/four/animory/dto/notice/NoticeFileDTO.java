package com.four.animory.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileDTO {
    private String uuid;
    private String fileName;
    private int ord;
    private boolean image;
}
