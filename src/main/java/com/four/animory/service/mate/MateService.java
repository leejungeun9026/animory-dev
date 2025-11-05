package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.dto.mate.MateBoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface MateService {
    List<MateBoardDTO> findAllMateBoards();
    void registerMateBoard(MateBoardDTO mateBoardDTO);



    default MateBoard dtoToEntity(MateBoardDTO mateBoardDTO) {
        MateBoard mateBoard = MateBoard.builder()
                .category(mateBoardDTO.getCategory())
                .state(mateBoardDTO.getState())
                .petInfo(mateBoardDTO.getPerInfo())
                .sido(mateBoardDTO.getSido())
                .title(mateBoardDTO.getTitle())
                .content(mateBoardDTO.getContent())
                .build();
        return mateBoard;
    }

    default MateBoardDTO entityToDTO(MateBoard mateBoard){
        MateBoardDTO mateBoardDTO = MateBoardDTO.builder()
                .bno(mateBoard.getBno())
                .state(mateBoard.getState())
                .perInfo(mateBoard.getPetInfo())
                .sido(mateBoard.getSido())
                .sigungu(mateBoard.getSigungu())
                .title(mateBoard.getTitle())
                .content(mateBoard.getContent())
                .readCount(mateBoard.getReadCount())
                .regDate(mateBoard.getRegDate())
                .updateDate(mateBoard.getUpdateDate())
                .build();
        return mateBoardDTO;
    }


}
