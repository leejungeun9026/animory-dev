package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeFile;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.repository.free.FreeBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FreeServiceImpl implements FreeService{
    @Autowired
    FreeBoardRepository freeBoardRepository;

    @Override // 글 등록하기
    public void registerFreeBoard(FreeBoardDTO freeBoardDTO) {
        FreeBoard freeBoard = dtoToEntity(freeBoardDTO);
        freeBoardRepository.save(freeBoard);
    }

    @Override // 게시글 db에서 가져와서 출력하기.
    public List<FreeBoardDTO> findAllFreeBoards() {
        List<FreeBoard> freeBoards = freeBoardRepository.findAll();
        List<FreeBoardDTO> freeBoardDTOS = new ArrayList<>();
        for (FreeBoard freeBoard : freeBoards) {
            freeBoardDTOS.add(entityToDTO(freeBoard));
        }
        return freeBoardDTOS;
    }

    @Override // bno로 게시글 1개 찾아오기
    public FreeBoardDTO findFreeBoardById(Long bno) {
        FreeBoard freeBoard = freeBoardRepository.findById(bno).orElse(null);
        FreeBoardDTO freeBoardDTO = entityToDTO(freeBoard);
        return freeBoardDTO;
    }

    @Override // 게시글 업데이트
    public void updateFreeBoard(FreeBoardDTO freeBoardDTO) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardDTO.getBno()).orElse(null); // 기존의 board 데이터 가져오기
        freeBoard.change(freeBoardDTO.getContent(), freeBoardDTO.getContent());
        freeBoardRepository.save(freeBoard);
    }


    @Override // 게시글 번호 삭제
    public void deleteFreeBoardById(Long bno) {
        freeBoardRepository.deleteById(bno);
    }


}
