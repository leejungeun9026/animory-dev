package com.four.animory.repository.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.repository.spr.search.SprBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SprSprBoardRepository extends JpaRepository<SprBoard, Long>, SprBoardSearch {
    @EntityGraph(attributePaths = {"fileSet"})
    @Query("select b from SprBoard  b where b.bno=:bno")
    Optional<SprBoard> findByBIdWithImages(Long bno);
}
