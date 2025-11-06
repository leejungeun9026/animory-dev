package com.four.animory.service.notice;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.repository.notice.NoticeRepository;
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
    private MemberRepository memberRepository; // 추가

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
        return board != null ? entityToDTO(board) : null;
    }

    @Override
    public NoticeBoardDTO findNoticeById(Long bno, int mode) {
        NoticeBoard noticeBoard = noticeRepository.findById(bno).orElse(null);
        if (noticeBoard == null) return null;

        if (mode == 1) {
            noticeBoard.updateReadCount();
            noticeRepository.save(noticeBoard);
        }
        return entityToDTO(noticeBoard);
    }

    @Override
    public void updateNotice(NoticeBoardDTO noticeBoardDTO) {
        NoticeBoard noticeBoard = noticeRepository.findById(noticeBoardDTO.getBno()).orElse(null);
        if (noticeBoard != null) {
            noticeBoard.change(noticeBoardDTO.getTitle(), noticeBoardDTO.getContent());
            noticeBoard.setPinned(noticeBoardDTO.isPinned());

            noticeRepository.save(noticeBoard);
        }
    }

    @Override
    public void removeNotice(Long bno) {
        noticeRepository.deleteById(bno);
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
                .map(this::entityToDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<NoticeBoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


}


