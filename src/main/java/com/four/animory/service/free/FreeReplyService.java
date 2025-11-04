//package com.four.animory.service.free;
//
//import com.four.animory.domain.free.FreeReply;
//import com.four.animory.dto.free.FreeReplyDTO;
//
//public interface FreeReplyService {
//    Long insertFreeReply(FreeReplyDTO freeReplyDTO);
//    FreeReplyDTO findById(Long rno);
//    void deleteFreeReply(Long rno);
//    void modifyFreeReply(FreeReplyDTO freeReplyDTO);
//
//    default FreeReply dtoToEntity(FreeReplyDTO freeReplyDTO) {
//        FreeReply freeReply = FreeReply.builder()
//                .rno(freeReplyDTO.getRno())
//                .content(freeReplyDTO.getContent())
//                .build();
//        return freeReply;
//    }
//    default FreeReplyDTO entityToDto(FreeReply freeReply) {
//        FreeReplyDTO freeReplyDTO = FreeReplyDTO.builder()
//                .rno(freeReply.getRno())
//                .content(freeReply.getContent())
//                .username(freeReply.getMember().getNickname())
//                .regDate(freeReply.getRegDate())
//                .updateDate(freeReply.getUpdateDate())
//                .bno(freeReply.getFreeBoard().getBno())
//                .build();
//        return freeReplyDTO;
//    }
//}
