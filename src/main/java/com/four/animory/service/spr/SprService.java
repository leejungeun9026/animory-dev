package com.four.animory.service.spr;


import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;

public interface SprService {
    void registerSprBoard(SprBoardDTO sprBoardDTO);
    PageResponseDTO<SprBoardDTO> getList(PageRequestDTO pageRequestDTO);
    SprBoardDTO findBoardById(Long bno, int mode);
    void updateBoard(SprBoardDTO sprBoardDTO);
    void deleteBoardById(Long bno);


    default SprBoard dtoToEntity(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = SprBoard.builder()
                .bno(sprBoardDTO.getBno())
                .category(sprBoardDTO.getCategory())
                .sido(sprBoardDTO.getSido())
                .sigungu(sprBoardDTO.getSigungu())
                .title(sprBoardDTO.getTitle())
                .content(sprBoardDTO.getContent())
                .complete(sprBoardDTO.isComplete())
                .dueDate(sprBoardDTO.getDueDate())
                .build();
        return sprBoard;
    }

    default SprBoardDTO entityToDTO(SprBoard sprBoard) {
        SprBoardDTO sprBoardDTO = SprBoardDTO.builder()
                .bno(sprBoard.getBno())
                .title(sprBoard.getTitle())
                .content(sprBoard.getContent())
                .author(sprBoard.getMember().getNickname())
                .readcount(sprBoard.getReadcount())
                .regDate(sprBoard.getRegDate())
                .updateDate(sprBoard.getUpdateDate())
                .sido(sprBoard.getSido())
                .sigungu(sprBoard.getSigungu())
                .dueDate(sprBoard.getDueDate())
                .category(sprBoard.getCategory())
                .recommend(sprBoard.getRecommend())
                .complete(sprBoard.isComplete())
                .build();
        return  sprBoardDTO;
    }
}
