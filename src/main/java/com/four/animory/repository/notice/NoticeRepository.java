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


    @Query(
            "SELECT n FROM NoticeBoard n " +
            "WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<NoticeBoard> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
