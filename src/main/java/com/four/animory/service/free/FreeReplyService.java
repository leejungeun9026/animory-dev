package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeReply;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.dto.free.FreePageResponseDTO;
import com.four.animory.dto.free.FreeReplyDTO;

import java.util.List;

public interface FreeReplyService {
    Long insertFreeReply(FreeReplyDTO freeReplyDTO, String username);
    FreeReplyDTO readFreeReply(Long rno);
    void modifyFreeReply(FreeReplyDTO freeReplyDTO);
    void deleteFreeReply(Long rno, String currentUser);
    List<FreeReply> getFreeReplies(Long bno);
    FreePageResponseDTO<FreeReplyDTO> getListOfFreeBoard(Long bno, FreePageRequestDTO freePageRequestDTO);

    default FreeReply dtoToEntity(FreeReplyDTO freeReplyDTO) {
        FreeReply freeReply = FreeReply.builder()
                .rno(freeReplyDTO.getRno())
                .content(freeReplyDTO.getContent())
                .build();
        return freeReply;
    }

    default FreeReplyDTO entityToDto(FreeReply freeReply) {
        FreeReplyDTO freeReplyDTO = FreeReplyDTO.builder()
                .rno(freeReply.getRno())
                .content(freeReply.getContent())
                .username(freeReply.getUsername())
                .nickname(freeReply.getNickname())
                .regDate(freeReply.getRegDate())
                .updateDate(freeReply.getUpdateDate())
                .bno(freeReply.getFreeBoard().getBno())
                .build();
        return freeReplyDTO;
    }

}
