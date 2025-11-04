package com.four.animory.service.notice;

import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.dto.notice.NoticeBoardDTO;
import com.four.animory.repository.notice.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;


    @Override
    public void registerNoticeBoard(NoticeBoardDTO dto) {
        NoticeBoard board = dtoToEntity(dto);
        noticeRepository.save(board);
    }

    @Override
    public Long insertNotice(NoticeBoardDTO noticeBoardDTO) {
        NoticeBoard board = dtoToEntity(noticeBoardDTO);
        Long bno = noticeRepository.save(board).getBno();
        return bno;
    }

    @Override
    public List<NoticeBoardDTO> findAllNotices() {
        List<NoticeBoard> boards = noticeRepository.findAll();
        List<NoticeBoardDTO> dtos = new ArrayList<>();
        for (NoticeBoard board : boards) {
            dtos.add(entityToDTO(board));
        }
        return dtos;
    }


    @Override
    public NoticeBoardDTO getNotice(Long bno) {
        NoticeBoard board = noticeRepository.findById(bno).orElse(null);
        if (board != null) {

            board.setReadCount(board.getReadCount() + 1);
            noticeRepository.save(board);
        }
        return entityToDTO(board);
    }

    @Override
    public void updateNotice(NoticeBoardDTO noticeBoardDTO) {
        NoticeBoard board = noticeRepository.findById(noticeBoardDTO.getBno()).orElse(null);
        if (board != null) {
            board.change(noticeBoardDTO.getTitle(), noticeBoardDTO.getContent());
            noticeRepository.save(board);
        }
    }

    @Override
    public void removeNotice(Long bno) {
        noticeRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<NoticeBoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<NoticeBoard> result = noticeRepository.findAll(pageable);

        List<NoticeBoardDTO> dtoList = result.getContent().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());

        int total = (int) result.getTotalElements();

        return PageResponseDTO.<NoticeBoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
}
