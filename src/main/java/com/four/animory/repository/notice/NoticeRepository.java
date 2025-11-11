package com.four.animory.repository.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.repository.notice.search.NoticeSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeBoard, Long>, NoticeSearch {

    @EntityGraph(attributePaths = {"fileSet"})

    List<NoticeBoard> findTop10ByOrderByIsPinnedDescBnoDesc();




}
