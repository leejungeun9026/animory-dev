package com.four.animory.service.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.domain.notice.NoticeReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeReplyDTO;

import java.util.List;

public interface NoticeReplyService {

    Long register(NoticeReplyDTO noticeReplyDTO, String username);
    NoticeReplyDTO read(Long rno);
    void modify(NoticeReplyDTO noticeReplyDTO);
    void remove(Long rno, String currentUser);
    List<NoticeReplyDTO> getReplies(Long bno);
    PageResponseDTO<NoticeReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);




    default NoticeReply dtoToEntity(NoticeReplyDTO dto) {
        return NoticeReply.builder()
                .rno(dto.getRno())
                .content(dto.getContent())
                .deleted(dto.isDeleted())
                .noticeBoard(NoticeBoard.builder().bno(dto.getBno()).build())
                .member(Member.builder().id(dto.getMno()).build())
                .build();
    }

    default NoticeReplyDTO entityToDTO(NoticeReply entity) {
        return NoticeReplyDTO.builder()
                .rno(entity.getRno())
                .content(entity.getContent())
                .deleted(entity.isDeleted())
                .regDate(entity.getRegDate())
                .updateDate(entity.getUpdateDate())
                .bno(entity.getNoticeBoard().getBno())
                .mno(entity.getMember().getId())
                .nickname(entity.getMember().getNickname())
                .build();
    }

}
