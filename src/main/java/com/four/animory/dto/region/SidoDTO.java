package com.four.animory.dto.region;

import com.four.animory.domain.region.SidoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SidoDTO {
    private Long id;
    private String name;

    // Entity → DTO 변환용 생성자
    public SidoDTO(SidoEntity sidoEntity) {
        this.id = sidoEntity.getId();
        this.name = sidoEntity.getName();
    }
}