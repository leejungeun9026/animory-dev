package com.four.animory.service.mate;


import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.*;
import com.four.animory.dto.spr.SprFileDTO;
import com.four.animory.repository.mate.MateBoardRepository;
import com.four.animory.repository.mate.MateReplyRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class MateServiceImpl implements MateService {
    @Autowired
    MateBoardRepository mateBoardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MateReplyRepository mateReplyRepository;

    //글 등록하기
    @Override
    public Long registerMateBoard(MateBoardDTO mateBoardDTO) {
        log.info("register mate board success1111 : mateBoardDTO=" + mateBoardDTO);
        MateBoard entity = dtoToEntity(mateBoardDTO);
        Member member = memberRepository.findByUsername(mateBoardDTO.getUsername());
        entity.setMember(member);
        MateBoard saved = mateBoardRepository.save(entity);
        return saved.getBno(); // PK 반환

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
        mateBoard.change(mateBoardDTO.getTitle(), mateBoardDTO.getContent(), mateBoardDTO.getCategory());
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
    public List<MateBoardDTO> getTop10MateBoardList() {
        List<MateBoard> boardList = mateBoardRepository.findTop10ByOrderByBnoDesc();
        return boardList.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int increaseLikeCountAndGet(Long bno) {
        MateBoard board = mateBoardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        board.setLikecount(board.getLikecount() + 1); // dirty checking으로 update
           return board.getLikecount();
    }

    // 검색 + 페이징 -> DTO 변환
    @Override
    public MatePageResponseDTO<MateBoardDTO> getList(MatePageRequestDTO matePageRequestDTO) {
        Pageable pageable = matePageRequestDTO.getPageable("bno"); //pageable 객체 만들었어 -> pagerequestDTO따라
        Page<MateBoard> result = mateBoardRepository.searchAll(
                matePageRequestDTO.getFields(),
                matePageRequestDTO.getKeyword(),
                pageable);

        List<MateBoardDTO> dtoList = result.getContent().stream()
                .map(board -> entityToDTO(board))
                .collect(Collectors.toList());
        int total = (int)result.getTotalElements();
        MatePageResponseDTO<MateBoardDTO> responseDTO=MatePageResponseDTO.<MateBoardDTO>withAll()
                .matePageRequestDTO(matePageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

        return responseDTO;
    }

    @Override
    public MatePageResponseDTO<MateReplyCountDTO> getListReplyCount(MatePageRequestDTO matePageRequestDTO) {
        String[] field = matePageRequestDTO.getFields();
        String keyword = matePageRequestDTO.getKeyword();
        Pageable pageable = matePageRequestDTO.getPageable("bno");
        Page<MateReplyCountDTO> result= mateBoardRepository.searchWithReplyCount(field, keyword, pageable);
        log.info(result.getContent());

        return MatePageResponseDTO.<MateReplyCountDTO>withAll()
                .matePageRequestDTO(matePageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
