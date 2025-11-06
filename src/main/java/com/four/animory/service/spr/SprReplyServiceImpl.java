package com.four.animory.service.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.spr.SprReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprReplyDTO;
import com.four.animory.repository.spr.SprBoardRepository;
import com.four.animory.repository.spr.SprReplyRepository;
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
public class SprReplyServiceImpl implements SprReplyService {
    @Autowired
    private SprReplyRepository sprReplyRepository;
    @Autowired
    private SprBoardRepository sprBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Long register(SprReplyDTO sprReplyDTO, String username) {
        SprReply sprReply = dtoToEntity(sprReplyDTO);
        SprBoard sprBoard = sprBoardRepository.findById(sprReplyDTO.getBno()).get();
        Member member = memberRepository.findByUsername(username);
        sprReply.setSprBoard(sprBoard);
        sprReply.setMember(member);
        Long rno = sprReplyRepository.save(sprReply).getRno();
        return rno;
    }

    @Override
    public SprReplyDTO read(Long rno) {
        SprReply sprReply = sprReplyRepository.findById(rno).get();
        SprReplyDTO sprReplyDTO = entityToDTO(sprReply);
        return sprReplyDTO;
    }

    @Override
    public void modify(SprReplyDTO sprReplyDTO) {
        SprReply sprReply = sprReplyRepository.findById(sprReplyDTO.getRno()).get();
        sprReply.setContent(sprReplyDTO.getContent());
        sprReplyRepository.save(sprReply);
    }

    @Override
    public void remove(Long rno, String currentUser) {
        SprReply sprReply = sprReplyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));
        if(!sprReply.getMember().getUsername().equals(currentUser)){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        sprReply.setDeleted(true);
        sprReply.setContent("삭제된 댓글입니다");
        sprReplyRepository.save(sprReply);
    }

    @Override
    public List<SprReply> getReplies(Long bno) {
        return sprReplyRepository.findBySprBoardBnoAndDeletedIsFalse(bno);
    }

    @Override
    public PageResponseDTO<SprReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("rno");
        Page<SprReply> result = sprReplyRepository.listOfBoard(bno, pageable);
        List<SprReplyDTO> dtoList = result.getContent().stream()
                .map(sprReply -> entityToDTO(sprReply))
                .collect(Collectors.toList());
        return PageResponseDTO.<SprReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
