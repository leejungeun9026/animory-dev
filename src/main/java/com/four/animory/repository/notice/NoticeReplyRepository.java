package com.four.animory.repository.notice;

import com.four.animory.domain.notice.NoticeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {

    //  페이징 목록 (게시글 bno 기준)
    @Query("select r from NoticeReply r where r.noticeBoard.bno = :bno")
    Page<NoticeReply> listOfBoard(Long bno, Pageable pageable);

    // 삭제되지 않은 댓글만 전체 조회
    List<NoticeReply> findByNoticeBoardBnoAndDeletedIsFalse(Long bno);

    //  해당 글의 댓글 일괄 삭제
    void deleteByNoticeBoard_Bno(Long bno);
}
