//package com.four.animory.service.free;
//
//import com.four.animory.domain.free.FreeBoard;
//import com.four.animory.domain.free.FreeReply;
//import com.four.animory.domain.user.Member;
//import com.four.animory.dto.common.PageRequestDTO;
//import com.four.animory.dto.common.PageResponseDTO;
//import com.four.animory.dto.free.FreeReplyDTO;
//import com.four.animory.repository.free.FreeBoardRepository;
//import com.four.animory.repository.free.FreeReplyRepository;
//import com.four.animory.repository.user.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Log4j2
//@Transactional
//@RequiredArgsConstructor
//public class FreeReplyServiceImpl implements FreeReplyService {
//    private FreeBoardRepository freeBoardRepository;
//    private FreeReplyRepository freeReplyRepository;
//    private MemberRepository memberRepository;
//
//    @Override // 댓글 입력 저장
//    public Long insertFreeReply(FreeReplyDTO freeReplyDTO) {
//        FreeReply freeReply = dtoToEntity(freeReplyDTO);
//        FreeBoard freeBoard  = freeBoardRepository.findById(freeReplyDTO.getBno()).orElse(null);
//        Member member = memberRepository.findByUsername(freeReplyDTO.getUsername());
//        freeReply.setFreeBoard(freeBoard);
//        freeReply.setMember(member);
//        Long rno = freeReplyRepository.save(freeReply).getRno();
//        return rno;
//    }
//    @Override // rno에 해당하는 댓글 찾음
//    public FreeReplyDTO findById(Long rno) {
//        FreeReply freeReply = freeReplyRepository.findById(rno).orElse(null);
//        return  entityToDto(freeReply);
//    }
//
//    @Override
//    public void deleteFreeReply(Long rno) {
//        freeReplyRepository.deleteById(rno);
//    }
//
//    @Override
//    public void modifyFreeReply(FreeReplyDTO freeReplyDTO) {
//        FreeReply freeReply = freeReplyRepository.findById(freeReplyDTO.getRno()).orElse(null);
//        freeReply.setContent(freeReplyDTO.getContent());
//        freeReplyRepository.save(freeReply);
//    }
//
//}
