package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import com.four.animory.dto.sitter.SitterBoardPageResponseDTO;

import java.util.List;

public interface SitterService {
  void insertSitterBoard(SitterBoardDTO sitterBoardDTO, Member member);
  List<SitterBoardDTO> getSitterBoardList();
  SitterBoardDTO getSitterBoardById(Long bno);
  SitterBoardPageResponseDTO<SitterBoardListDTO> getSitterBoardListSearchPage(SitterBoardPageRequestDTO sitterBoardPageRequestDTO);

  default SitterBoard dtoToEntity(SitterBoardDTO sitterBoardDTO){
    SitterBoard sitterBoard = SitterBoard.builder()
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
        .state(sitterBoard.getState())
        .category(sitterBoard.getCategory())
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
