package com.four.animory.repository.spr;

import com.four.animory.domain.spr.SprBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprRepository extends JpaRepository<SprBoard, Long> {
}
