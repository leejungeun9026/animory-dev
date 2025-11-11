package com.four.animory.service.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.domain.notice.NoticeFile;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.dto.notice.NoticeFileDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface NoticeService {

    void registerNotice(NoticeBoardDTO noticeBoardDTO);
    NoticeBoardDTO getNotice(Long bno);
    NoticeBoardDTO findNoticeById(Long bno, int mode);
    void updateNotice(NoticeBoardDTO dto);
    void removeNotice(Long bno);
    List<NoticeBoardDTO> getTop10NoticeBoardList();

    PageResponseDTO<NoticeBoardDTO> getList(PageRequestDTO pageRequestDTO);


    default NoticeBoard dtoToEntity(NoticeBoardDTO dto) {
        NoticeBoard noticeBoard = NoticeBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .readCount(dto.getReadCount())
                .isPinned(dto.isPinned())
                .build();

        if (dto.getNoticeFileDTOs() != null) {
            dto.getNoticeFileDTOs().forEach(file ->
                    noticeBoard.addFile(file.getUuid(), file.getFileName(), file.isImage())
            );
        }

        return noticeBoard;
    }

    // ✅ entityToDTO - return 뒤 코드 정리, 변수명 수정
    default NoticeBoardDTO entityToDTO(NoticeBoard entity) {
        NoticeBoardDTO noticeBoardDTO = NoticeBoardDTO.builder()
                .bno(entity.getBno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .readCount(entity.getReadCount())
                .nickname(entity.getMember() != null ? entity.getMember().getNickname() : "admin")
                .isPinned(entity.isPinned())
                .regDate(entity.getRegDate())
                .updateDate(entity.getUpdateDate())
                .build();

        if (entity.getFileSet() != null) {
            List<NoticeFileDTO> fileDTOs = entity.getFileSet().stream()
                    .sorted()
                    .map(this::fileEntityToDTO)
                    .collect(Collectors.toList());
            noticeBoardDTO.setNoticeFileDTOs(fileDTOs);
        }

        return noticeBoardDTO;
    }


    default NoticeFileDTO fileEntityToDTO(NoticeFile noticeFile) {
        return NoticeFileDTO.builder()
                .uuid(noticeFile.getUuid())
                .fileName(noticeFile.getFilename())
                .image(noticeFile.isImage())
                .ord(noticeFile.getOrd())
                .build();
    }
}
