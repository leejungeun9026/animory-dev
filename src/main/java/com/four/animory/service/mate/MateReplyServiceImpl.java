package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.mate.MateReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.MateReplyDTO;
import com.four.animory.repository.mate.MateBoardRepository;
import com.four.animory.repository.mate.MateReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional


public class MateReplyServiceImpl implements MateReplyService {
    @Autowired
    private MateReplyRepository mateReplyRepository;
    @Autowired
    private MateBoardRepository mateBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public long getReplyCount(Long bno) {
        return mateReplyRepository.countByMateBoard_Bno(bno);
    }

    @Override
    public Long register(MateReplyDTO mateReplyDTO, String username) {
        MateReply mateReply = dtoToEntity(mateReplyDTO);
        MateBoard mateBoard = mateBoardRepository.findById(mateReplyDTO.getBno()).get();
        Member member = memberRepository.findByUsername(username);
        mateReply.setMateBoard(mateBoard);
        mateReply.setMember(member);
        Long rno = mateReplyRepository.save(mateReply).getRno();
        return rno;
    }

    @Override
    public MateReplyDTO read(Long rno) {
        MateReply mateReply = mateReplyRepository.findById(rno).get();
        MateReplyDTO mateReplyDTO = entityToDTO(mateReply);
        return mateReplyDTO;
    }

    @Override
    public void modify(MateReplyDTO mateReplyDTO) {
        MateReply mateReply = mateReplyRepository.findById(mateReplyDTO.getRno()).get();
        mateReply.setContent(mateReplyDTO.getContent());
        mateReplyRepository.save(mateReply);
    }

    @Override
    public void remove(Long rno, String currentUser) {
        MateReply mateReply = mateReplyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));
        if(!mateReply.getMember().getUsername().equals(currentUser)){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        mateReply.setDeleted(true);
        mateReply.setContent("삭제된 댓글입니다");
        mateReplyRepository.save(mateReply);
    }

    @Override
    public List<MateReply> getReplies(Long bno) {
        return mateReplyRepository.findByMateBoardBnoAndDeletedIsFalse(bno);
    }

    @Override
    public PageResponseDTO<MateReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("rno");
        Page<MateReply> result = mateReplyRepository.listOfBoard(bno, pageable);
        List<MateReplyDTO> dtoList = result.getContent().stream()
                .map(mateReply -> entityToDTO(mateReply))
                .collect(Collectors.toList());
        return PageResponseDTO.<MateReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
