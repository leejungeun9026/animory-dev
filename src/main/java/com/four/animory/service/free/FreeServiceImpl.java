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

    @Override
    public FreeBoardDTO findFreeBoardById(Long bno) {
        FreeBoard freeBoard = freeBoardRepository.findById(bno).orElse(null);
        FreeBoardDTO freeBoardDTO = entityToDTO(freeBoard);
        return freeBoardDTO;
    }



}
