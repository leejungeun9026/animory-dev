package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SitterBoardRepository extends JpaRepository<SitterBoard, Long>, SitterSearch {
  @EntityGraph(attributePaths = {"fileSet"})
  @Query("select b from SitterBoard b where b.bno = :bno")
  Optional<SitterBoard> findByIdWithImages(long bno);

  @Query("""
    select new com.four.animory.dto.sitter.SitterBoardListDTO(
      b.bno,
      b.state,
      b.category,
      b.petInfo,
      b.sido,
      b.sigungu,
      b.title,
      m.nickname,
      b.readCount,
      cast(coalesce(count(r), 0) as long),
      b.regDate
    )
    from SitterBoard b
      left join b.member m
      left join SitterReply r on r.sitterBoard = b
    group by
      b.bno, b.state, b.category, b.petInfo, b.sido, b.sigungu,
      b.title, m.nickname, b.readCount, b.regDate
    order by b.regDate desc
  """)
  List<SitterBoardListDTO> findRecentWithReplyCount(Pageable pageable);
}