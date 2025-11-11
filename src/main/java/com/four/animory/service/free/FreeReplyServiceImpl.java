package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.dto.free.FreePageResponseDTO;
import com.four.animory.dto.free.FreeReplyDTO;
import com.four.animory.repository.free.FreeBoardRepository;
import com.four.animory.repository.free.FreeReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FreeReplyServiceImpl implements FreeReplyService {
    private final FreeReplyRepository freeReplyRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertFreeReply(FreeReplyDTO freeReplyDTO, String username) {
        FreeReply freeReply = dtoToEntity(freeReplyDTO);
        FreeBoard freeBoard = freeBoardRepository.findById(freeReplyDTO.getBno()).orElse(null);
        Member member = memberRepository.findByUsername(username);
        freeReply.setFreeBoard(freeBoard);
        freeReply.setMember(member);

        if(freeReplyDTO.getParentRno() != null){
            FreeReply parent = freeReplyRepository.findById(freeReplyDTO.getParentRno()).orElse(null);
            freeReply.setParent(parent);
        }
        Long rno = freeReplyRepository.save(freeReply).getRno();
        return rno;
    }

    @Override
    public FreeReplyDTO readFreeReply(Long rno) {
        FreeReply freeReply = freeReplyRepository.findById(rno).get();
        FreeReplyDTO freeReplyDTO = entityToDto(freeReply);
        return freeReplyDTO;
    }

    @Override
    public void modifyFreeReply(FreeReplyDTO freeReplyDTO) {
        FreeReply freeReply = freeReplyRepository.findById(freeReplyDTO.getRno()).orElse(null);
        freeReply.setContent(freeReplyDTO.getContent());
        freeReplyRepository.save(freeReply);
    }

    @Override
    public void deleteFreeReply(Long rno, String currentUser, String loginRole) {
        FreeReply freeReply = freeReplyRepository.findById(rno).orElseThrow(()-> new IllegalArgumentException("댓글이 없습니다."));

        if(loginRole.equals("ADMIN")){
            freeReply.setDeleted(true);
            freeReply.setContent("삭제된 댓글입니다.");
            freeReplyRepository.save(freeReply);
            return;
        }
        if(!freeReply.getMember().getUsername().equals(currentUser)){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        freeReply.setDeleted(true);
        freeReply.setContent("삭제된 댓글입니다");
        freeReplyRepository.save(freeReply);
    }

    @Override
    public List<FreeReply> getFreeReplies(Long bno) {
        return freeReplyRepository.findByFreeBoard_BnoAndDeletedIsFalse(bno);
    }

    @Override
    public FreePageResponseDTO<FreeReplyDTO> getListOfFreeBoard(Long bno, FreePageRequestDTO freePageRequestDTO) {
        Pageable pageable = freePageRequestDTO.getPageable("rno");
        Page<FreeReply> result = freeReplyRepository.listOfBoard(bno, pageable);
        List<FreeReplyDTO> dtoList = result.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return FreePageResponseDTO.<FreeReplyDTO>withAll()
                .freePageRequestDTO(freePageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
