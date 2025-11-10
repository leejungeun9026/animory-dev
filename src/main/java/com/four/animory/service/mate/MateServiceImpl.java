package com.four.animory.service.mate;


import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.repository.mate.MateBoardRepository;
import com.four.animory.repository.mate.MateReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.four.animory.domain.mate.QMateBoard.mateBoard;

@Service
@RequiredArgsConstructor
@Transactional
public class MateServiceImpl implements MateService {
    @Autowired
    MateBoardRepository mateBoardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MateReplyRepository mateReplyRepository;

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
        MateBoard mateBoard = mateBoardRepository.findByIdWithImages(bno).orElse(null);
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
        MateBoard mateBoard = mateBoardRepository.findById(bno).orElse(null);
        mateBoard.removeFile();
        mateReplyRepository.deleteByMateBoardId(bno);
        mateBoardRepository.deleteById(bno);
    }

    @Override
    public MateBoardDTO updateLikecount(Long bno) {
        MateBoard mateBoard = mateBoardRepository.findById(bno).orElse(null);
        mateBoard.updateLikecount();
        mateBoardRepository.save(mateBoard);
        MateBoardDTO dto = entityToDTO(mateBoard);
        return dto;
    }

    @Override
    public List<MateBoard> getTop10MateBoardList() {
        return mateBoardRepository.findTop10ByOrderByBnoDesc();
    }

    // 검색 + 페이징 -> DTO 변환


    @Override
    public PageResponseDTO<MateBoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno"); //pageable 객체 만들었어 -> pagerequestDTO따라
        Page<MateBoard> result = mateBoardRepository.searchAll(
                pageRequestDTO.getTypes(),
                pageRequestDTO.getKeyword(),
                pageable); //동적 쿼리를 통해 검색해 오고 아래의 내용을 추출해 LIST에 넣어

        List<MateBoardDTO> dtoList = result.getContent().stream()
                .map(board -> entityToDTO(board))
                .collect(Collectors.toList());
        int total = (int)result.getTotalElements(); //total record count
        PageResponseDTO<MateBoardDTO> responseDTO=PageResponseDTO.<MateBoardDTO>withAll() //BoardDTO 객체로 만들거야
                .pageRequestDTO(pageRequestDTO) //DTO에 있는 정보 + LIST + TOTAL 정보 넣고 BUILD
                .dtoList(dtoList)
                .total(total)
                .build();

        return responseDTO;
    }

}
