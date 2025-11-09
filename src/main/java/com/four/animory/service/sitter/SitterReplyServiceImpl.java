package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.sitter.SitterReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.sitter.SitterReplyDTO;
import com.four.animory.repository.sitter.SitterBoardRepository;
import com.four.animory.repository.sitter.SitterReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SitterReplyServiceImpl implements SitterReplyService {
  private final MemberRepository memberRepository;
  private final SitterBoardRepository sitterBoardRepository;
  private final SitterReplyRepository sitterReplyRepository;

  @Override
  public void insertReply(SitterReplyDTO sitterReplyDTO) {
    SitterBoard sitterBoard = sitterBoardRepository.findById(sitterReplyDTO.getBno()).orElse(null);
    Member member = memberRepository.findByUsername(sitterReplyDTO.getUsername());
    SitterReply sitterReply = dtoToEntity(sitterReplyDTO);
    sitterReply.setSitterBoard(sitterBoard);
    sitterReply.setMember(member);
    sitterReplyRepository.save(sitterReply);
  }

  @Override
  public List<SitterReplyDTO> findAllByBno(Long bno) {
    List<SitterReply> sitterReplyList = sitterReplyRepository.findAllByBno(bno);
    List<SitterReplyDTO> sitterReplyDTOList = new ArrayList<>();
    for (SitterReply sitterReply : sitterReplyList) {
      sitterReplyDTOList.add(entityToDTO(sitterReply));
    }
    return sitterReplyDTOList;
  }

  @Override
  public SitterReplyDTO getReply(Long rno) {
    SitterReply sitterReply = sitterReplyRepository.findById(rno).orElse(null);
    log.info(sitterReply);
    return entityToDTO(sitterReply);
  }

  @Override
  public void updateReply(SitterReplyDTO sitterReplyDTO) {
    SitterReply sitterReply = sitterReplyRepository.findById(sitterReplyDTO.getRno()).orElse(null);
    log.info(sitterReply);
    sitterReply.setContent(sitterReplyDTO.getContent());
    sitterReplyRepository.save(sitterReply);
  }

  @Override
  public void deleteReply(Long rno) {
    sitterReplyRepository.deleteById(rno);
  }
}
