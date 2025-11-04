package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.repository.sitter.SitterRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SitterServiceImpl implements SitterService {
  @Autowired
  private SitterRepository sitterRepository;
  @Autowired
  private MemberRepository memberRepository;

  @Override
  public void registerSitterBoard(SitterBoardDTO sitterBoardDTO) {
    SitterBoard board = dtoToEntity(sitterBoardDTO);
    Member member = memberRepository.findByUsername("test");
    board.setMember(member);
    sitterRepository.save(board);
  }
}
