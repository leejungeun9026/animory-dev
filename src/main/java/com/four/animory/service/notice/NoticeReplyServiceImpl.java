package com.four.animory.service.notice;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.domain.notice.NoticeReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.notice.NoticeReplyDTO;
import com.four.animory.repository.notice.NoticeReplyRepository;
import com.four.animory.repository.notice.NoticeRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NoticeReplyServiceImpl implements NoticeReplyService{
    @Autowired
    private NoticeReplyRepository noticeReplyRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Override
    public Long register(NoticeReplyDTO noticeReplyDTO, String username) {
        NoticeReply noticeReply = dtoToEntity(noticeReplyDTO);
        NoticeBoard noticeBoard = noticeRepository.findById(noticeReplyDTO.getBno()).get();
        Member member = memberRepository.findByUsername(username);
        noticeReply.setMember(member);
        noticeReply.setNoticeBoard(noticeBoard);
        Long rno = noticeReplyRepository.save(noticeReply).getRno();
        return rno;
    }

    @Override
    public NoticeReplyDTO read(Long rno) {
        NoticeReply noticeReply = noticeReplyRepository.findById(rno).get();
        NoticeReplyDTO noticeReplyDTO = entityToDTO(noticeReply);
        return noticeReplyDTO;
    }

    @Override
    public void modify(NoticeReplyDTO noticeReplyDTO) {
        NoticeReply noticeReply = noticeReplyRepository.findById(noticeReplyDTO.getRno()).get();
        noticeReply.setContent(noticeReplyDTO.getContent());
        noticeReplyRepository.save(noticeReply);

    }

    @Override
    public void remove(Long rno, String currentUser) {
        NoticeReply noticeReply = noticeReplyRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));

        // 로그인한 사용자 정보 가져오기
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

        // 작성자가 아니고 관리자도 아닐 경우 예외
        if (!noticeReply.getMember().getUsername().equals(currentUser) && !isAdmin) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        noticeReply.setDeleted(true);
        noticeReply.setContent("삭제된 댓글입니다");
        noticeReplyRepository.save(noticeReply);
    }


    @Override
    public List<NoticeReplyDTO> getReplies(Long bno) {
        return noticeReplyRepository.findByNoticeBoardBnoAndDeletedIsFalse(bno)
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public PageResponseDTO<NoticeReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("rno");
        Page<NoticeReply> result=noticeReplyRepository.listOfBoard(bno,pageable);
        List<NoticeReplyDTO>dtoList = result.getContent().stream()
                .map(noticeReply -> entityToDTO(noticeReply))
                .collect(Collectors.toList());
        return PageResponseDTO.<NoticeReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
