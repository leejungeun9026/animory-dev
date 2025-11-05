package com.four.animory.repository.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.spr.SprReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprReplyRepository extends JpaRepository<SprReply,Long> {
    @Query("select r from SprReply r where r.sprBoard.bno=:bno")
    Page<SprReply> listOfBoard(Long bno, Pageable pageable);

    List<SprReply> findBySprBoardBnoAndDeletedIsFalse(Long bno);
    void deleteBySprBoard_Bno(Long bno);
}
