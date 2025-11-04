package com.four.animory.dto.region;

import com.four.animory.domain.region.SigunguEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigunguDTO {
    private Long id;
    private String name;
    private Long sidoId;   // 부모 시·도의 id

    // Entity → DTO 변환용 생성자
    public SigunguDTO(SigunguEntity sigunguEntity) {
        this.id = sigunguEntity.getId();
        this.name = sigunguEntity.getName();
        this.sidoId = sigunguEntity.getSido().getId();
    }
}