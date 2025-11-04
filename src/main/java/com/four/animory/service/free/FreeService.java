package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.dto.free.FreeBoardDTO;

import java.util.List;


public interface FreeService {
    void registerFreeBoard(FreeBoardDTO freeBoardDTO); // 글 등록
    List<FreeBoardDTO> findAllFreeBoards(); // 게시글 전부 가져오기
    FreeBoardDTO findFreeBoardById(Long bno);

    // dto -> Entity
    default FreeBoard dtoToEntity(FreeBoardDTO freeBoardDTO) {
        FreeBoard freeBoard = FreeBoard.builder()
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .writer(freeBoardDTO.getWriter())
                .btype(freeBoardDTO.getBtype())
                .likecount(freeBoardDTO.getLikecount())
                .readcount(freeBoardDTO.getReadcount())
                .build();
        return freeBoard;
    }

    //Entity -> DTO
    default FreeBoardDTO entityToDTO(FreeBoard freeBoard) {
        FreeBoardDTO freeBoardDTO = FreeBoardDTO.builder()
                .bno(freeBoard.getBno())
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .writer(freeBoard.getWriter())
                .btype(freeBoard.getBtype())
                .likecount(freeBoard.getLikecount())
                .readcount(freeBoard.getReadcount())
                .replycount(freeBoard.getReplies()!= null ? freeBoard.getReplies().size():0)
                .regDate(freeBoard.getRegDate())
                .updateDate(freeBoard.getUpdateDate())
                .build();
        return freeBoardDTO;
    }
}
