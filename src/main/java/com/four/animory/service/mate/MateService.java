package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.dto.mate.MateBoardDTO;


public interface MateService {
    void regesterMateBoard(MateBoardDTO mateBoardDTO);

//    default MateBoardDTO dtoToEntity(MateBoardDTO mateBoardDTO) {
//        MateBoardDTO mateBoardDTO1 = MateBoard.builder()
//                .category(mateBoardDTO.getCategory())
//                .state(mateBoardDTO.getState())
//                .petInfo(mateBoardDTO.getPerInfo())
//                .sido(mateBoardDTO.getSido())
//                .title(mateBoardDTO.getTitle())
//                .content(mateBoardDTO.getContent())
//                .build();
////        return mateBoard;
//    }
}
