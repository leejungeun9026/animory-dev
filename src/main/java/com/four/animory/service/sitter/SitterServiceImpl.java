package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import com.four.animory.dto.sitter.SitterBoardPageResponseDTO;
import com.four.animory.repository.sitter.SitterRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SitterServiceImpl implements SitterService {
  @Autowired
  private SitterRepository sitterRepository;
  @Autowired
  private MemberRepository memberRepository;

  @Override
  public void insertSitterBoard(SitterBoardDTO sitterBoardDTO, Member member) {
    SitterBoard board = dtoToEntity(sitterBoardDTO);
    board.setMember(member);
    sitterRepository.save(board);
  }

  @Override
  public List<SitterBoardDTO> getSitterBoardList() {
    List<SitterBoard> sitterBoardList = sitterRepository.findAll();
    List<SitterBoardDTO> sitterBoardDTOList = new ArrayList<>();
    for(SitterBoard sitterBoard : sitterBoardList) {
      sitterBoardDTOList.add(entityToDTO(sitterBoard));
    }
    return sitterBoardDTOList;
  }

  @Override
  public SitterBoardDTO getSitterBoardById(Long bno) {
    SitterBoard board = sitterRepository.findById(bno).orElse(null);
    return entityToDTO(Objects.requireNonNull(board));
  }

  @Override
  public SitterBoardPageResponseDTO<SitterBoardListDTO> getSitterBoardListSearchPage(SitterBoardPageRequestDTO sitterBoardPageRequestDTO) {
    String sido = sitterBoardPageRequestDTO.getSido();
    String sigungu = sitterBoardPageRequestDTO.getSigungu();
    String category = sitterBoardPageRequestDTO.getCategory();
    String state = sitterBoardPageRequestDTO.getState();
    String petInfo = sitterBoardPageRequestDTO.getPetInfo();
    String[] fields = sitterBoardPageRequestDTO.getFields();
    String keyword = sitterBoardPageRequestDTO.getKeyword();

    Pageable pageable = sitterBoardPageRequestDTO.getPageable("bno");
    Page<SitterBoardListDTO> result = sitterRepository.search(sido, sigungu, category, state, petInfo, fields, keyword, pageable);

    return SitterBoardPageResponseDTO.<SitterBoardListDTO>withAll()
        .pageRequestDTO(sitterBoardPageRequestDTO)
        .dtoList(result.getContent())
        .total((int)result.getTotalElements())
        .build();
  }


}
