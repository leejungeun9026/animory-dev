package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterReply;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterReplyDTO;

import java.util.List;

public interface SitterReplyService {
  void insertReply(SitterReplyDTO sitterReplyDTO);
  List<SitterReplyDTO> findAllByBno(Long bno);

  default SitterReply dtoToEntity(SitterReplyDTO sitterReplyDTO){
    return SitterReply.builder()
        .rno(sitterReplyDTO.getRno())
        .content(sitterReplyDTO.getContent())
        .isHiring(sitterReplyDTO.isHiring())
        .build();
  }

  default SitterReplyDTO entityToDTO(SitterReply sitterReply){
    return SitterReplyDTO.builder()
        .bno(sitterReply.getSitterBoard().getBno())
        .rno(sitterReply.getRno())
        .content(sitterReply.getContent())
        .nickname(sitterReply.getMember().getNickname())
        .username(sitterReply.getMember().getUsername())
        .userTel(sitterReply.getMember().getTel())
        .mid(sitterReply.getMember().getId())
        .isHiring(sitterReply.isHiring())
        .regDate(sitterReply.getRegDate())
        .build();
  }
}
