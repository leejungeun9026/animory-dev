package com.four.animory.repository.free;

import com.four.animory.domain.free.FreeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeReplyRepository extends JpaRepository<FreeReply, Long> {
    // 게시글 댓글 페이징
    @Query("select fr from FreeReply fr where fr.freeBoard.bno=:bno")
    Page<FreeReply> listOfBoard(Long bno, Pageable pageable);

    List<FreeReply> findByFreeBoard_BnoAndDeletedIsFalse(Long bno);
    void deleteByFreeBoard_Bno(Long bno);

}
