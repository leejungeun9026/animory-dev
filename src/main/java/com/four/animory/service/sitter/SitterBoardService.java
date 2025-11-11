package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.sitter.SitterFile;
import com.four.animory.dto.sitter.*;
import com.four.animory.dto.sitter.file.SitterUploadResultDTO;
import com.four.animory.dto.user.MemberDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface SitterBoardService {
  void insertSitterBoard(SitterBoardDTO sitterBoardDTO, MemberDTO memberDTO);
  SitterBoardDTO getSitterBoardById(Long bno, String mode);
  SitterBoardPageResponseDTO<SitterBoardListDTO> getSitterBoardListSearchPage(SitterBoardPageRequestDTO sitterBoardPageRequestDTO);
  void updateBoard(SitterBoardDTO sitterBoardDTO);
    int deleteBoard(Long bno);
  List<SitterBoardListDTO> getRecent(int count);

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
    if(sitterBoardDTO.getFileDTOs() != null){
      sitterBoardDTO.getFileDTOs().forEach(fileDTO -> {
        sitterBoard.addFiles(fileDTO.getUuid(), fileDTO.getFilename(), fileDTO.isImage());
      });
    }
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
    List<SitterFileDTO> sitterFileDTOs = sitterBoard.getFileSet().stream()
        .sorted()
        .map(file -> fileEntityToDTO(file))
        .collect(Collectors.toList());
    sitterBoardDTO.setFileDTOs(sitterFileDTOs);
    return sitterBoardDTO;
  }

  default SitterFileDTO fileEntityToDTO(SitterFile sitterFile){
    return SitterFileDTO.builder()
        .uuid(sitterFile.getUuid())
        .filename(sitterFile.getFilename())
        .image(sitterFile.isImage())
        .ord(sitterFile.getOrd())
        .build();
  }
}
