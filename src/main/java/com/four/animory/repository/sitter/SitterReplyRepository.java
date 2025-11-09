package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.SitterReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SitterReplyRepository extends JpaRepository<SitterReply,Long> {
  @Query("select r from SitterReply r where r.sitterBoard.bno = :bno")
  List<SitterReply> findAllByBno(Long bno);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from SitterReply r where r.sitterBoard.bno = :bno")
  void deleteAllReplyByBno(Long bno);
}
