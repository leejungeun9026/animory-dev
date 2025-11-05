package com.four.animory.service.mate;


import com.four.animory.domain.mate.MateBoard;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.repository.mate.MateBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class MateServiceImpl implements MateService {
    @Autowired
    private MateBoardRepository mateBoardRepository;


    //개시글 DB에서 가져와서 출력하기
    @Override
    public List<MateBoardDTO> findAllMateBoards() {
        List<MateBoard> mateBoards = mateBoardRepository.findAll();
        List<MateBoardDTO> mateBoardDTOS = new ArrayList<>();
        for (MateBoard mateBoard : mateBoards) {
            mateBoardDTOS.add(entityToDTO(mateBoard));
        }
        log.info(mateBoardDTOS);
        return mateBoardDTOS;
    }

    // 글 등록하기
    @Override
    public void registerMateBoard(MateBoardDTO mateBoardDTO) {
        MateBoard matoBoard = dtoToEntity(mateBoardDTO);
        mateBoardRepository.save(matoBoard);
    }




}
