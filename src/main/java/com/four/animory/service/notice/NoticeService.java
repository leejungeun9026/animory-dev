package com.four.animory.service.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import org.springframework.data.domain.Page;

public interface NoticeService {

    Long registerNotice(NoticeBoardDTO noticeBoardDTO);
    NoticeBoardDTO getNotice(Long bno);
    NoticeBoardDTO findNoticeById(Long bno, int mode);
    void updateNotice(NoticeBoardDTO dto);
    void removeNotice(Long bno);



    PageResponseDTO<NoticeBoardDTO> getList(PageRequestDTO pageRequestDTO);

    default NoticeBoard dtoToEntity(NoticeBoardDTO dto){
        return NoticeBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .readCount(dto.getReadCount())
                .isPinned(dto.isPinned())
                .build();
    }

    default NoticeBoardDTO entityToDTO(NoticeBoard entity){
        return NoticeBoardDTO.builder()
                .bno(entity.getBno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .readCount(entity.getReadCount())
                .isPinned(entity.isPinned())
                .regDate(entity.getRegDate())
                .updateDate(entity.getUpdateDate())
                .build();
    }
}
