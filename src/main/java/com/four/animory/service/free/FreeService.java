package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeFile;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.*;
import com.four.animory.dto.free.FreeBoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;


public interface FreeService {
    void registerFreeBoard(FreeBoardDTO freeBoardDTO, Member member); // 글 등록
    List<FreeBoardDTO> findAllFreeBoards(); // 게시글 전부 가져오기
    FreeBoardDTO findFreeBoardById(Long bno,Integer mode);
    void updateFreeBoard(FreeBoardDTO freeBoardDTO);
    void deleteFreeBoardById(Long bno);
    FreeBoardDTO updateLikecount(Long bno);

    FreePageResponseDTO<FreeBoardDTO> getList(FreePageRequestDTO freePageRequestDTO);
    FreePageResponseDTO<FreeBoardListReplyCountDTO> getListReplyCount(FreePageRequestDTO freePageRequestDTO);


    // dto -> Entity
    default FreeBoard dtoToEntity(FreeBoardDTO freeBoardDTO) {
        FreeBoard freeBoard = FreeBoard.builder()
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .btype(freeBoardDTO.getBtype())
                .likecount(freeBoardDTO.getLikecount())
                .readcount(freeBoardDTO.getReadcount())
                .build();

        if(freeBoardDTO.getFreeFileDTOS() !=null){
            freeBoardDTO.getFreeFileDTOS().forEach(filename -> {
//                String[] arr = filename.split("_");
                freeBoard.addFile(filename.getUuid(), filename.getFilename(), filename.isImage());
            });
        }
        return freeBoard;
    }

    //Entity -> DTO
    default FreeBoardDTO entityToDTO(FreeBoard freeBoard) {
        FreeBoardDTO freeBoardDTO = FreeBoardDTO.builder()
                .bno(freeBoard.getBno())
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .nickname(freeBoard.getMember().getNickname())
                .username(freeBoard.getMember().getUsername())
                .btype(freeBoard.getBtype())
                .likecount(freeBoard.getLikecount())
                .readcount(freeBoard.getReadcount())
                .replycount(freeBoard.getReplies()!= null ? freeBoard.getReplies().size():0)
                .regDate(freeBoard.getRegDate())
                .updateDate(freeBoard.getUpdateDate())
                .build();

        List<FreeFileDTO> freeFileDTO = freeBoard.getFileSet()
                .stream()
                .sorted()
                .map(img->fileEntityToDTO(img))
                .collect(Collectors.toList());
        freeBoardDTO.setFreeFileDTOS(freeFileDTO);
        return freeBoardDTO;
    }

    default FreeFileDTO fileEntityToDTO(FreeFile freeFile) {
        FreeFileDTO dto = FreeFileDTO.builder()
                .uuid(freeFile.getUuid())
                .filename(freeFile.getFilename())
                .image(freeFile.isImage())
                .ord(freeFile.getOrd())
                .build();
        return dto;
    }

}
