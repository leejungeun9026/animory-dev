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
        NoticeReply reply = noticeReplyRepository.findById(noticeReplyDTO.getRno())
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 현재 로그인 사용자 이름 가져오기
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Member me = memberRepository.findByUsername(currentUser);
        if (me == null) {
            throw new AccessDeniedException("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isAdmin = me.getRole() != null &&
                ("ROLE_ADMIN".equalsIgnoreCase(me.getRole()) || "ADMIN".equalsIgnoreCase(me.getRole()));
        boolean isOwner = reply.getMember() != null &&
                currentUser.equals(reply.getMember().getUsername());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        reply.setContent(noticeReplyDTO.getContent());
        noticeReplyRepository.save(reply);


    }

    @Override
    public void remove(Long rno, String currentUser) {
        NoticeReply reply = noticeReplyRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. rno=" + rno));

        Member me = memberRepository.findByUsername(currentUser);
        if (me == null) throw new AccessDeniedException("사용자 정보가 없습니다.");

        boolean isAdmin = me.getRole() != null &&
                ("ROLE_ADMIN".equalsIgnoreCase(me.getRole()) || "ADMIN".equalsIgnoreCase(me.getRole()));

        boolean isOwner = reply.getMember() != null &&
                reply.getMember().getId() != null &&
                me.getId() != null &&
                reply.getMember().getId().equals(me.getId());

        if (!isOwner && !isAdmin) throw new AccessDeniedException("삭제 권한이 없습니다.");

        reply.setDeleted(true);
        reply.setContent(isAdmin ? "관리자가 삭제한 댓글입니다." : "삭제된 댓글입니다.");
        noticeReplyRepository.save(reply);
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
        Page<NoticeReply> result = noticeReplyRepository.listOfBoard(bno, pageable);

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Member me = memberRepository.findByUsername(currentUser);

        List<NoticeReplyDTO> dtoList = result.getContent().stream()
                .map(entity -> {
                    NoticeReplyDTO dto = entityToDTO(entity);

                    dto.setOwner(entity.getMember() != null &&
                            entity.getMember().getUsername().equals(currentUser));

                    dto.setAdmin(me != null && me.getRole() != null &&
                            (me.getRole().equalsIgnoreCase("ADMIN") ||
                                    me.getRole().equalsIgnoreCase("ROLE_ADMIN")));

                    return dto;
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<NoticeReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }



}
