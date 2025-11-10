package com.four.animory.repository.mate;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.mate.MateBoard;
import com.four.animory.repository.sitter.SitterSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateBoardRepository extends JpaRepository<MateBoard, Long>, MateSearch{
    List<MateBoard> bno(Long bno);
    List<MateBoard> findTop10ByOrderByBnoDesc();
    @EntityGraph(attributePaths = {"fileSet"})
    @Query("select b from MateBoard b where b.bno=:bno")
    Optional<MateBoard> findByIdWithImages(Long bno);

}
