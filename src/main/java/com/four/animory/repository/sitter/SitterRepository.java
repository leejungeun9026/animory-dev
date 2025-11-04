package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitterRepository extends JpaRepository<SitterBoard, Long> {
}
