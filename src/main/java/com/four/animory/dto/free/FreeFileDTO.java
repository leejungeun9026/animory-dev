package com.four.animory.dto.free;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeFileDTO {
    private String uuid;
    private String filename;
    private int ord;
    private boolean image;
}