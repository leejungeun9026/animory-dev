package com.four.animory.service.mate;


import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.repository.mate.MateBoardRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MateServiceImpl implements MateService {
    @Autowired
    MateBoardRepository mateBoardRepository;
    @Autowired
    MemberRepository memberRepository;

    //글 등록하기
    @Override
    public void registerMateBoard(MateBoardDTO mateBoardDTO, Member member) {
        MateBoard mateBoard = dtoToEntity(mateBoardDTO);
        mateBoard.setMember(member);
        mateBoardRepository.save(mateBoard); //entity를 리턴 -> 레파지토리에 저장
    }

    //게시글 db에서 가져와서 출력하기.
    @Override
    public List<MateBoardDTO> findAllMateBoards() {
        List<MateBoard> mateBoards = mateBoardRepository.findAll(Sort.by(Sort.Direction.DESC, "updateDate"));
        List<MateBoardDTO> mateBoardDTOS = new ArrayList<>();
        for (MateBoard mateBoard : mateBoards) {
            mateBoardDTOS.add(entityToDTO(mateBoard));
        }
        return mateBoardDTOS;
    }

    @Override
    public MateBoardDTO findMateBoardById(Long bno, Integer mode) {
        MateBoard mateBoard = mateBoardRepository.findById(bno).orElse(null);
        if(mode == 1){
            mateBoard.updateReadCount();
            mateBoardRepository.save(mateBoard);
        }
        MateBoardDTO mateBoardDTO = entityToDTO(mateBoard);
        mateBoardDTO.setUsername(mateBoard.getMember().getUsername());
        return mateBoardDTO;
    }

    @Override // 게시글 업데이트
    public void updateMateBoard(MateBoardDTO mateBoardDTO) {
        MateBoard mateBoard = mateBoardRepository.findById(mateBoardDTO.getBno()).orElse(null); // 기존의 board 데이터 가져오기
        mateBoard.change(mateBoardDTO.getContent(), mateBoardDTO.getContent(), mateBoardDTO.getCategory());
        mateBoardRepository.save(mateBoard);
    }

    @Override
    public void deleteMateBoardById(Long bno) {
        mateBoardRepository.deleteById(bno);

    }

}
