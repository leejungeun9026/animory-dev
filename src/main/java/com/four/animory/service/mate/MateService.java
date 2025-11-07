package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.mate.MateBoardDTO;

import java.util.List;

public interface MateService {
    void registerMateBoard(MateBoardDTO mateBoardDTO, Member member);
    List<MateBoardDTO> findAllMateBoards();
    MateBoardDTO findMateBoardById(Long bno, Integer mode);
    void updateMateBoard(MateBoardDTO mateBoardDTO);
    void deleteMateBoardById(Long bno);


    default MateBoard dtoToEntity(MateBoardDTO mateBoardDTO) {
        MateBoard mateBoard = MateBoard.builder()
                .category(mateBoardDTO.getCategory())
                .sido(mateBoardDTO.getSido())
                .sigungu(mateBoardDTO.getSigungu())
                .title(mateBoardDTO.getTitle())
                .content(mateBoardDTO.getContent())
                .build();
        return mateBoard;
    }

    default MateBoardDTO entityToDTO(MateBoard mateBoard){
        MateBoardDTO mateBoardDTO = MateBoardDTO.builder()
                .bno(mateBoard.getBno())

                .category(mateBoard.getCategory())

                .sido(mateBoard.getSido())
                .sigungu(mateBoard.getSigungu())
                .title(mateBoard.getTitle())
                .content(mateBoard.getContent())
                .username(mateBoard.getMember().getUsername())
                .nickname(mateBoard.getMember().getNickname())
                .readCount(mateBoard.getReadCount())
                .regDate(mateBoard.getRegDate())
                .updateDate(mateBoard.getUpdateDate())
                .build();
        return mateBoardDTO;
    }


}
