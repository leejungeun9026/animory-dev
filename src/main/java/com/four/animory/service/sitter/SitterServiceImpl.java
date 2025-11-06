package com.four.animory.service.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import com.four.animory.dto.sitter.SitterBoardPageResponseDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.repository.sitter.SitterRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class SitterServiceImpl implements SitterService {
  @Autowired
  private SitterRepository sitterRepository;
  @Autowired
  private MemberRepository memberRepository;

  @Override
  public void insertSitterBoard(SitterBoardDTO sitterBoardDTO, MemberDTO memberDTO) {
    if(sitterBoardDTO.getCategory().equals("구해요")){
      sitterBoardDTO.setState("구인중");
    } else if (sitterBoardDTO.getCategory().equals("일해요")){
      sitterBoardDTO.setState("구직중");
    }
    SitterBoard board = dtoToEntity(sitterBoardDTO);
    board.setMember(memberRepository.findByUsername(memberDTO.getUsername()));
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
  public SitterBoardDTO getSitterBoardById(Long bno, String mode) {
    SitterBoard board = sitterRepository.findById(bno).orElse(null);
    if(mode.equals("1")){
      board.updateReadCount();
      sitterRepository.save(board);
    }
    return entityToDTO(board);
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
    log.info(sitterBoardPageRequestDTO);
    log.info(result);

    return SitterBoardPageResponseDTO.<SitterBoardListDTO>withAll()
        .sitterBoardPageRequestDTO(sitterBoardPageRequestDTO)
        .dtoList(result.getContent())
        .total((int)result.getTotalElements())
        .build();
  }

  @Override
  public void updateBoard(SitterBoardDTO sitterBoardDTO) {
    SitterBoard board = sitterRepository.findById(sitterBoardDTO.getBno()).orElse(null);
    board.changeBoard(sitterBoardDTO);
    log.info(board);
    sitterRepository.save(board);
  }

  @Override
  public int deleteBoard(Long bno) {
    sitterRepository.deleteById(bno);
    SitterBoard result = sitterRepository.findById(bno).orElse(null);
    if (result == null){
      return 1;
    } else {
      return 0;
    }
  }
}
