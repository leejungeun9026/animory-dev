package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeReply;
import com.four.animory.dto.free.*;

import java.util.List;

public interface FreeReplyService {
    Long insertFreeReply(FreeReplyDTO freeReplyDTO, String username);
    FreeReplyDTO readFreeReply(Long rno);
    void modifyFreeReply(FreeReplyDTO freeReplyDTO);
    void deleteFreeReply(Long rno, String currentUser, String loginRole);
    List<FreeReply> getFreeReplies(Long bno);
    FreePageResponseDTO<FreeReplyDTO> getListOfFreeBoard(Long bno, FreePageRequestDTO freePageRequestDTO);

        default FreeReply dtoToEntity(FreeReplyDTO freeReplyDTO) {
            FreeBoard freeBoard = FreeBoard.builder().bno(freeReplyDTO.getBno()).build();
            FreeReply parent = null;
            if(freeReplyDTO.getParentRno() !=null){
                parent = FreeReply.builder().rno(freeReplyDTO.getParentRno()).build();
            }
            FreeReply freeReply = FreeReply.builder()
                    .rno(freeReplyDTO.getRno())
                    .content(freeReplyDTO.getContent())
                    .deleted(freeReplyDTO.isDeleted())
                    .parent(parent)
                    .build();
            return freeReply;
        }

        default FreeReplyDTO entityToDto(FreeReply freeReply) {
            FreeReplyDTO.FreeReplyDTOBuilder builder = FreeReplyDTO.builder()
                    .rno(freeReply.getRno())
                    .content(freeReply.getContent())
                    .deleted(freeReply.isDeleted())
                    .username(freeReply.getMember().getUsername())
                    .nickname(freeReply.getMember().getNickname())
                    .regDate(freeReply.getRegDate())
                    .updateDate(freeReply.getUpdateDate())
                    .mid(freeReply.getMember().getId())
                    .bno(freeReply.getFreeBoard().getBno());
                    if(freeReply.getParent() !=null){
                        builder.parentRno(freeReply.getParent().getRno());
                        builder.parentUsername(freeReply.getParent().getMember().getUsername());
                        builder.parentNickname(freeReply.getParent().getMember().getNickname());
                    }else {
                        builder.parentRno(null);
                        builder.parentUsername(null);
                        builder.parentNickname(null);
                    }
                    return builder.build();
        }






}
