package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.dto.sitter.SitterBoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface SitterService {
  void registerSitterBoard(SitterBoardDTO sitterBoardDTO);

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
        .petInfo(sitterBoard.getPetInfo())
        .sido(sitterBoard.getSido())
        .sigungu(sitterBoard.getSigungu())
        .title(sitterBoard.getTitle())
        .content(sitterBoard.getContent())
        .readCount(sitterBoard.getReadCount())
        .regDate(sitterBoard.getRegDate())
        .updateDate(sitterBoard.getUpdateDate())
        .build();
    return sitterBoardDTO;
  }
}
