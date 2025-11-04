package com.four.animory.repository.region;

import com.four.animory.domain.region.SidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoRepository extends JpaRepository<SidoEntity, Long> {
}
