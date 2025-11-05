package com.four.animory.repository.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.repository.spr.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprBoardRepository extends JpaRepository<SprBoard, Long>, BoardSearch {
}
