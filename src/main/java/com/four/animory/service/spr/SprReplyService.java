package com.four.animory.service.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.spr.SprReply;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprReplyDTO;

import java.util.List;

public interface SprReplyService {
    Long register(SprReplyDTO sprReplyDTO, String username);
    SprReplyDTO read(Long rno);
    void modify(SprReplyDTO sprReplyDTO);
    void remove(Long rno, String currentUser, String loginRole);
    List<SprReply> getReplies(Long bno);
    PageResponseDTO<SprReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    default com.four.animory.domain.spr.SprReply dtoToEntity(SprReplyDTO sprReplyDTO) {
       SprBoard sprBoard = SprBoard.builder().bno(sprReplyDTO.getBno()).build();
       SprReply parent = null;
       if(sprReplyDTO.getParentRno() != null){
           parent = SprReply.builder().rno(sprReplyDTO.getParentRno()).build();
       }
        com.four.animory.domain.spr.SprReply sprReply = com.four.animory.domain.spr.SprReply.builder()
                .rno(sprReplyDTO.getRno())
                .content(sprReplyDTO.getContent())
                .secret(sprReplyDTO.isSecret())
                .deleted(sprReplyDTO.isDeleted())
                .parent(parent)
                .build();
        return sprReply;
    }

    default SprReplyDTO entityToDTO(com.four.animory.domain.spr.SprReply sprReply) {
        SprReplyDTO.SprReplyDTOBuilder builder = SprReplyDTO.builder()
                .rno(sprReply.getRno())
                .content(sprReply.getContent())
                .secret(sprReply.isSecret())
                .deleted(sprReply.isDeleted())
                .author(sprReply.getMember().getNickname())
                .updateDate(sprReply.getUpdateDate())
                .regDate(sprReply.getRegDate())
                .bno(sprReply.getSprBoard().getBno())
                .mid(sprReply.getMember().getId())
                .username(sprReply.getMember().getUsername());
        if(sprReply.getParent() !=null) {
            builder.parentRno(sprReply.getParent().getRno());
            builder.parentUsername(sprReply.getParent().getMember().getUsername());
            builder.parentAuthor(sprReply.getParent().getMember().getNickname());
        }else{
            builder.parentRno(null);
            builder.parentUsername(null);
            builder.parentAuthor(null);
        }

        return builder.build();

    }
}
