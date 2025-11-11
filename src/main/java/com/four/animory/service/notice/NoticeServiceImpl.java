package com.four.animory.service.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.dto.notice.NoticeFileDTO;
import com.four.animory.repository.notice.NoticeRepository;
import com.four.animory.repository.notice.NoticeReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NoticeReplyRepository noticeReplyRepository; //  댓글 수 계산용

    @Override
    public void registerNotice(NoticeBoardDTO noticeBoardDTO) {
        NoticeBoard noticeBoard = dtoToEntity(noticeBoardDTO);
        Member member = memberRepository.findByUsername(noticeBoardDTO.getNickname());
        noticeBoard.setMember(member);
        noticeRepository.save(noticeBoard);
    }

    @Override
    public NoticeBoardDTO getNotice(Long bno) {
        NoticeBoard board = noticeRepository.findById(bno).orElse(null);
        if (board == null) return null;

        NoticeBoardDTO dto = entityToDTO(board);

        //  댓글 수 계산 (DB에는 저장 안 됨)
        int replyCount = noticeReplyRepository.countByNoticeBoardBnoAndDeletedIsFalse(bno);
        dto.setReplyCount(replyCount);

        return dto;
    }

    @Override
    public NoticeBoardDTO findNoticeById(Long bno, int mode) {
        NoticeBoard noticeBoard = noticeRepository.findById(bno).orElse(null);
        if (noticeBoard == null) return null;

        if (mode == 1) {
            noticeBoard.updateReadCount();
            noticeRepository.save(noticeBoard);
        }

        NoticeBoardDTO dto = entityToDTO(noticeBoard);

        //  댓글 수 계산 추가
        int replyCount = noticeReplyRepository.countByNoticeBoardBnoAndDeletedIsFalse(bno);
        dto.setReplyCount(replyCount);

        return dto;
    }

    @Override
    public void updateNotice(NoticeBoardDTO noticeBoardDTO) {
        NoticeBoard noticeBoard = noticeRepository.findById(noticeBoardDTO.getBno())
                .orElseThrow(() -> new IllegalArgumentException("공지 없음: " + noticeBoardDTO.getBno()));

        // 본문/고정 변경
        noticeBoard.change(noticeBoardDTO.getTitle(), noticeBoardDTO.getContent());
        noticeBoard.setPinned(noticeBoardDTO.isPinned());

        // 파일 교체 (DTO에 파일 리스트가 있을 때만)
        if (noticeBoardDTO.getNoticeFileDTOs() != null) {
            noticeBoard.removeFile(); // 기존 첨부 제거
            for (NoticeFileDTO noticeFileDTO : noticeBoardDTO.getNoticeFileDTOs()) {
                noticeBoard.addFile(noticeFileDTO.getUuid(), noticeFileDTO.getFileName(), noticeFileDTO.isImage());
            }
        }

        noticeRepository.save(noticeBoard);
    }

    @Override
    public void removeNotice(Long bno) {
        NoticeBoard noticeBoard = noticeRepository.findById(bno).orElse(null);
        if (noticeBoard == null) return;

        // 첨부 제거(엔티티 내부 컬렉션/파일 정리)
        noticeBoard.removeFile();
        // 댓글 일괄 삭제(진짜 삭제; 소프트면 soft 처리로 바꿔도 됨)
        noticeReplyRepository.deleteByNoticeBoard_Bno(bno);
        // 본문 삭제
        noticeRepository.deleteById(bno);
    }


    @Override
    public List<NoticeBoardDTO> getTop10NoticeBoardList() {
        List<NoticeBoard>noticeBoards = noticeRepository.findTop10ByOrderByIsPinnedDescBnoDesc();
        return noticeBoards.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<NoticeBoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<NoticeBoard> result = noticeRepository.searchAll(
                pageRequestDTO.getTypes(),
                pageRequestDTO.getKeyword(),
                pageable
        );






        var dtoList = result.getContent().stream()
                .map(board -> {
                    NoticeBoardDTO dto = entityToDTO(board);

                    //  각 게시글의 댓글 수 계산
                    int replyCount = noticeReplyRepository.countByNoticeBoardBnoAndDeletedIsFalse(board.getBno());
                    dto.setReplyCount(replyCount);

                    return dto;
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<NoticeBoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
