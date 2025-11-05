package com.four.animory.repository.notice;

import com.four.animory.domain.notice.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeBoard, Long> {


    @Query("""
  SELECT n FROM NoticeBoard n
  WHERE (:kw IS NULL OR :kw = ''
         OR LOWER(n.title)   LIKE LOWER(CONCAT('%', :kw, '%'))
         OR LOWER(n.content) LIKE LOWER(CONCAT('%', :kw, '%')))
  ORDER BY n.isPinned DESC, n.bno DESC
""")
    Page<NoticeBoard> search(@Param("kw") String keyword, Pageable pageable);


}
