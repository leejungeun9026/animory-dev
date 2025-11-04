package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateRepository extends JpaRepository<MateBoard, Long> {

}
