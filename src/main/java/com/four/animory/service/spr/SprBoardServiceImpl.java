package com.four.animory.service.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.repository.spr.SprBoardRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprBoardServiceImpl implements SprBoardService {
    @Autowired
    private SprBoardRepository sprRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void registerSprBoard(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = dtoToEntity(sprBoardDTO);
        Member member = memberRepository.findByUsername(sprBoardDTO.getAuthor());
        sprBoard.setMember(member);
        sprRepository.save(sprBoard);
    }

    @Override
    public PageResponseDTO<SprBoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<SprBoardDTO> result =  sprRepository.searchAll(
                pageRequestDTO.getTypes(), pageRequestDTO.getKeyword(), pageable);
        PageResponseDTO<SprBoardDTO> responseDTO = PageResponseDTO.<SprBoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
        return responseDTO;
    }

    @Override
    public SprBoardDTO findBoardById(Long bno, int mode) {
        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        if(mode==1){
            sprBoard.updateReadCount();
            sprRepository.save(sprBoard);
        }
        SprBoardDTO dto = entityToDTO(sprBoard);
        return dto;
    }

    @Override
    public void updateBoard(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = sprRepository.findById(sprBoardDTO.getBno()).orElse(null);
        sprBoard.change(sprBoardDTO.getTitle(), sprBoardDTO.getContent(), sprBoardDTO.isComplete());
        sprRepository.save(sprBoard);
    }

    @Override
    public void deleteBoardById(Long bno) {
//        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        sprRepository.deleteById(bno);
    }

    @Override
    public SprBoardDTO upedateRecommend(Long bno) {
        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        sprBoard.updateRecommend();
        sprRepository.save(sprBoard);
        SprBoardDTO dto = entityToDTO(sprBoard);
        return dto;
    }

}
