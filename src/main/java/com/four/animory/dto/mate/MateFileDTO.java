package com.four.animory.dto.mate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MateFileDTO {
    private String uuid;
    private String fileName;
    private int ord;
    private boolean image;
}
