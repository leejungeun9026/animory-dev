package com.four.animory.service.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.mate.MateFile;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.dto.mate.MateFileDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface MateService {
    void registerMateBoard(MateBoardDTO mateBoardDTO, Member member);
    List<MateBoardDTO> findAllMateBoards();
    MateBoardDTO findMateBoardById(Long bno, Integer mode);
    void updateMateBoard(MateBoardDTO mateBoardDTO);
    void deleteMateBoardById(Long bno);
    PageResponseDTO<MateBoardDTO> getList(PageRequestDTO pageRequestDTO);
//    PageResponseDTO<BoardListReplyCountDTO> getListReplyCount(PageRequestDTO pageRequestDTO);


    default MateBoard dtoToEntity(MateBoardDTO mateBoardDTO) {
        MateBoard mateBoard = MateBoard.builder()
                .category(mateBoardDTO.getCategory())
                .sido(mateBoardDTO.getSido())
                .sigungu(mateBoardDTO.getSigungu())
                .title(mateBoardDTO.getTitle())
                .content(mateBoardDTO.getContent())
                .dueDate(mateBoardDTO.getDueDate())
                .build();

        if(mateBoardDTO.getMateFileDTOs() != null){
            mateBoardDTO.getMateFileDTOs().forEach(file-> {
                mateBoard.addFile(file.getUuid(), file.getFileName(), file.isImage());
            });
        }
        return mateBoard;
    }

    default MateBoardDTO entityToDTO(MateBoard mateBoard){
        MateBoardDTO mateBoardDTO = MateBoardDTO.builder()
                .bno(mateBoard.getBno())
                .title(mateBoard.getTitle())
                .content(mateBoard.getContent())
                .readCount(mateBoard.getReadCount())
                .regDate(mateBoard.getRegDate())
                .updateDate(mateBoard.getUpdateDate())
                .sido(mateBoard.getSido())
                .sigungu(mateBoard.getSigungu())
                .dueDate(mateBoard.getDueDate())
                .category(mateBoard.getCategory())
                .username(mateBoard.getMember().getUsername())
                .nickname(mateBoard.getMember().getNickname())
                .build();
        //이미지 첨부를 위해 추가
        List<MateFileDTO> mateFileDTOList = mateBoard.getFileSet().stream()
                .sorted()
                .map(image->fileEntityToDTO(image))
                .collect(Collectors.toList()); //리스트 만들어
        mateBoardDTO.setMateFileDTOs(mateFileDTOList);




        return mateBoardDTO;
    }
    default MateFileDTO fileEntityToDTO(MateFile mateFile) {
        MateFileDTO mateFileDTO = MateFileDTO.builder()
                .uuid(mateFile.getUuid())
                .fileName(mateFile.getFileName())
                .image(mateFile.isImage())
                .ord(mateFile.getOrd())
                .build();
        return mateFileDTO;
    }

}
