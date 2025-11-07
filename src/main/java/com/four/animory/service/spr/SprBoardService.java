package com.four.animory.service.spr;


import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.spr.SprFile;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.dto.spr.SprFileDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface SprBoardService {
    void registerSprBoard(SprBoardDTO sprBoardDTO);
    PageResponseDTO<SprBoardDTO> getList(PageRequestDTO pageRequestDTO);
    SprBoardDTO findBoardById(Long bno, int mode);
    void updateBoard(SprBoardDTO sprBoardDTO);
    void deleteBoardById(Long bno);
    SprBoardDTO updateRecommend(Long bno);


    default SprBoard dtoToEntity(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = SprBoard.builder()
                .bno(sprBoardDTO.getBno())
                .category(sprBoardDTO.getCategory())
                .sido(sprBoardDTO.getSido())
                .sigungu(sprBoardDTO.getSigungu())
                .title(sprBoardDTO.getTitle())
                .content(sprBoardDTO.getContent())
                .complete(sprBoardDTO.isComplete())
                .dueDate(sprBoardDTO.getDueDate())
                .build();
        if(sprBoardDTO.getSprFileDTOs() != null){
            sprBoardDTO.getSprFileDTOs().forEach(file -> {
                sprBoard.addFile(file.getUuid(), file.getFileName(), file.isImage());
            });
        }
        return sprBoard;
    }

    default SprBoardDTO entityToDTO(SprBoard sprBoard) {
        SprBoardDTO sprBoardDTO = SprBoardDTO.builder()
                .bno(sprBoard.getBno())
                .title(sprBoard.getTitle())
                .content(sprBoard.getContent())
                .author(sprBoard.getMember().getNickname())
                .readcount(sprBoard.getReadcount())
                .regDate(sprBoard.getRegDate())
                .updateDate(sprBoard.getUpdateDate())
                .sido(sprBoard.getSido())
                .sigungu(sprBoard.getSigungu())
                .dueDate(sprBoard.getDueDate())
                .category(sprBoard.getCategory())
                .recommend(sprBoard.getRecommend())
                .complete(sprBoard.isComplete())
                .username(sprBoard.getMember().getUsername())
                .build();
        List<SprFileDTO> sprFileDTOList = sprBoard.getFileSet().stream()
                .sorted()
                .map(file -> fileEntityToDTO(file))
                .collect(Collectors.toList());
        sprBoardDTO.setSprFileDTOs(sprFileDTOList);
        return  sprBoardDTO;
    }

    default SprFileDTO fileEntityToDTO(SprFile sprFile) {
        SprFileDTO sprFileDTO = SprFileDTO.builder()
                .uuid(sprFile.getUuid())
                .fileName(sprFile.getFileName())
                .image(sprFile.isImage())
                .ord(sprFile.getOrd())
                .build();
        return sprFileDTO;
    }
}
