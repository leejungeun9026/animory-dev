package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MateReplyRepository extends JpaRepository<MateReply, Long> {
    @Query("select  r from MateReply r where r.mateBoard.bno=:bno")
    Page<MateReply> listOfBoard(Long bno, Pageable pageable);
    long countByMateBoard_Bno(Long bno);
    @Modifying
    @Query("delete from MateReply r where r.mateBoard.bno=:bno")
    void deleteByMateBoardId(Long bno);

    List<MateReply> findByMateBoardBnoAndDeletedIsFalse(Long mateBoard);
    void deleteByMateBoard_Bno(Long bno);


}
