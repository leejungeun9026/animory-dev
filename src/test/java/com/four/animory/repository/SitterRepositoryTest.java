package com.four.animory.repository;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.sitter.SitterReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterReplyDTO;
import com.four.animory.repository.sitter.SitterBoardRepository;
import com.four.animory.repository.sitter.SitterReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
@Log4j2
public class SitterRepositoryTest {
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private SitterBoardRepository sitterBoardRepository;
  @Autowired
  private SitterReplyRepository sitterReplyRepository;

  @Test
  public void saveTest(){
    Member member = memberRepository.findByUsername("test");
    SitterBoard sitterBoard = sitterBoardRepository.findById(12L).orElse(null);
    SitterReply sitterReply = SitterReply.builder()
        .sitterBoard(sitterBoard)
        .member(member)
        .content("댓글 작성 테스트")
        .build();
    sitterReplyRepository.save(sitterReply);
  }

  @Test
  public void findAllByBnoTest(){
    int count = sitterReplyRepository.findAllByBno(12L).size();
    List<SitterReply> sitterReplyList = sitterReplyRepository.findAllByBno(12L);
    log.info(count);
    log.info(sitterReplyList);
  }

  @Test
  public void findRecentWithReplyCountTest(){
    Pageable top10 = PageRequest.of(0, 10);
    List<SitterBoardListDTO> list = sitterBoardRepository.findRecentWithReplyCount(top10);
    for (SitterBoardListDTO sitterBoardListDTO : list) {
      log.info(sitterBoardListDTO);
    }
  }
}
