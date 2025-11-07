package com.four.animory.service.spr;

import com.four.animory.domain.spr.SprReply;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprReplyDTO;

import java.util.List;

public interface SprReplyService {
    Long register(SprReplyDTO sprReplyDTO, String username);
    SprReplyDTO read(Long rno);
    void modify(SprReplyDTO sprReplyDTO);
    void remove(Long rno, String currentUser);
    List<SprReply> getReplies(Long bno);
    PageResponseDTO<SprReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    default com.four.animory.domain.spr.SprReply dtoToEntity(SprReplyDTO sprReplyDTO) {
        com.four.animory.domain.spr.SprReply sprReply = com.four.animory.domain.spr.SprReply.builder()
                .rno(sprReplyDTO.getRno())
                .content(sprReplyDTO.getContent())
                .secret(sprReplyDTO.isSecret())
                .deleted(sprReplyDTO.isDeleted())
                .build();
        return sprReply;
    }

    default SprReplyDTO entityToDTO(com.four.animory.domain.spr.SprReply sprReply) {
        SprReplyDTO sprReplyDTO = SprReplyDTO.builder()
                .rno(sprReply.getRno())
                .content(sprReply.getContent())
                .secret(sprReply.isSecret())
                .deleted(sprReply.isDeleted())
                .author(sprReply.getMember().getNickname())
                .updateDate(sprReply.getUpdateDate())
                .regDate(sprReply.getRegDate())
                .bno(sprReply.getSprBoard().getBno())
                .mid(sprReply.getMember().getId())
                .build();
        return sprReplyDTO;

    }
}
