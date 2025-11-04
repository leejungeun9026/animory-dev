package com.four.animory.repository.region;

import com.four.animory.domain.region.SigunguEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SigunguRepository extends JpaRepository<SigunguEntity, Long> {
    List<SigunguEntity> findBySido_IdOrderByNameAsc(Long sidoId);
}
