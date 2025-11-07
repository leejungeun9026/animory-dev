package com.four.animory.dto.spr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprFileDTO {
    private String uuid;
    private String fileName;
    private int ord;
    private boolean image;
}
