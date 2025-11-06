package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import com.four.animory.dto.sitter.SitterBoardPageResponseDTO;
import com.four.animory.dto.user.MemberDTO;

import java.util.List;

public interface SitterService {
  void insertSitterBoard(SitterBoardDTO sitterBoardDTO, MemberDTO memberDTO);
  List<SitterBoardDTO> getSitterBoardList();
  SitterBoardDTO getSitterBoardById(Long bno, String mode);
  SitterBoardPageResponseDTO<SitterBoardListDTO> getSitterBoardListSearchPage(SitterBoardPageRequestDTO sitterBoardPageRequestDTO);
  void updateBoard(SitterBoardDTO sitterBoardDTO);
  int deleteBoard(Long bno);

  default SitterBoard dtoToEntity(SitterBoardDTO sitterBoardDTO){
    SitterBoard sitterBoard = SitterBoard.builder()
        .bno(sitterBoardDTO.getBno())
        .category(sitterBoardDTO.getCategory())
        .state(sitterBoardDTO.getState())
        .petInfo(sitterBoardDTO.getPetInfo())
        .sido(sitterBoardDTO.getSido())
        .sigungu(sitterBoardDTO.getSigungu())
        .title(sitterBoardDTO.getTitle())
        .content(sitterBoardDTO.getContent())
        .build();
    return sitterBoard;
  }

  default SitterBoardDTO entityToDTO(SitterBoard sitterBoard){
    SitterBoardDTO sitterBoardDTO = SitterBoardDTO.builder()
        .bno(sitterBoard.getBno())
        .category(sitterBoard.getCategory())
        .state(sitterBoard.getState())
        .petInfo(sitterBoard.getPetInfo())
        .sido(sitterBoard.getSido())
        .sigungu(sitterBoard.getSigungu())
        .title(sitterBoard.getTitle())
        .content(sitterBoard.getContent())
        .username(sitterBoard.getMember().getUsername())
        .nickname(sitterBoard.getMember().getNickname())
        .readCount(sitterBoard.getReadCount())
        .regDate(sitterBoard.getRegDate())
        .updateDate(sitterBoard.getUpdateDate())
        .build();
    return sitterBoardDTO;
  }
}
