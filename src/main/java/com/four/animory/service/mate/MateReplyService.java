package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.mate.MateReply;
import com.four.animory.domain.mate.MateReply;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.MateReplyDTO;


import java.util.List;

public interface MateReplyService {
    Long register(MateReplyDTO mateReplyDTO, String username);
    MateReplyDTO read(Long rno);
    void modify(MateReplyDTO mateReplyDTO);
    void remove(Long rno, String currentUser);
    List<MateReply> getReplies(Long bno);
    PageResponseDTO<MateReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
    long getReplyCount(Long bno);

    default com.four.animory.domain.mate.MateReply dtoToEntity(MateReplyDTO mateReplyDTO) {
        com.four.animory.domain.mate.MateReply mateReply = com.four.animory.domain.mate.MateReply.builder()
                .rno(mateReplyDTO.getRno())
                .content(mateReplyDTO.getContent())
                .secret(mateReplyDTO.isSecret())
                .deleted(mateReplyDTO.isDeleted())
                .build();
        return mateReply;
    }

    default MateReplyDTO entityToDTO(com.four.animory.domain.mate.MateReply mateReply) {
        MateReplyDTO mateReplyDTO = MateReplyDTO.builder()
                .rno(mateReply.getRno())
                .content(mateReply.getContent())
                .secret(mateReply.isSecret())
                .deleted(mateReply.isDeleted())
                .nickname(mateReply.getMember().getNickname())
                .updateDate(mateReply.getUpdateDate())
                .regDate(mateReply.getRegDate())
                .bno(mateReply.getMateBoard().getBno())
                .mid(mateReply.getMember().getId())
                .username(mateReply.getMember().getUsername())
                .build();
        return mateReplyDTO;

    }
}
