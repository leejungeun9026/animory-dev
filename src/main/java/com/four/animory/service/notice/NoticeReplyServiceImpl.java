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
        // 1) 댓글 조회
        NoticeReply reply = noticeReplyRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. rno=" + rno));

        // 2) 현재 사용자 조회 (DB 기준으로 역할 판단)
        Member me = memberRepository.findByUsername(currentUser);
        if (me == null) {
            throw new AccessDeniedException("사용자 정보가 없습니다.");
        }

        // 3) 관리자 여부: ROLE_ADMIN / ADMIN 둘 다 허용
        String role = me.getRole(); // 예: "ROLE_ADMIN" 또는 "ADMIN"
        boolean isAdmin = role != null && (
                "ROLE_ADMIN".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)
        );

        // 4) 본인 댓글이거나 관리자면 삭제 허용
        boolean isOwner = reply.getMember() != null
                && currentUser.equals(reply.getMember().getUsername());



        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        // 5) 소프트 삭제
        reply.setDeleted(true);
        if(isAdmin) {
            reply.setContent("관리자가 삭제한 댓글입니다.");
        }else{
            reply.setContent("삭제된 댓글입니다.");
        }

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
