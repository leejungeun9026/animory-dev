package com.four.animory.repository.free;

import com.four.animory.domain.free.FreeBoard;
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
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeSearch {
    List<FreeBoard> bno(Long bno);

    @EntityGraph(attributePaths = {"fileSet"}) // FreeBoard 엔티티에 연관된 imageSet을 한 번에 같이 가져오겠다는 뜻
    @Query("select fb from FreeBoard fb where fb.bno=:bno")
    Optional<FreeBoard> findByIdWithImages(Long bno);

    List<FreeBoard> findTop10ByOrderByBnoDesc();

}
