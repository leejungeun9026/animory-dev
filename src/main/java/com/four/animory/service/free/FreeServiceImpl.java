package com.four.animory.service.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.free.FreeFileDTO;
import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.dto.free.FreePageResponseDTO;
import com.four.animory.dto.free.FreeBoardListReplyCountDTO;
import com.four.animory.repository.free.FreeBoardRepository;
import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FreeServiceImpl implements FreeService{
    @Autowired
    FreeBoardRepository freeBoardRepository;
    @Autowired
    MemberRepository memberRepository;

    @Override // 글 등록하기
    public void registerFreeBoard(FreeBoardDTO freeBoardDTO, Member member) {
        FreeBoard freeBoard = dtoToEntity(freeBoardDTO);
        freeBoard.setMember(member);
        freeBoardRepository.save(freeBoard); // save는 저장한 Entity를 리턴
    }

    @Override // 게시글 db에서 가져와서 출력하기.
    public List<FreeBoardDTO> findAllFreeBoards() {
        List<FreeBoard> freeBoards = freeBoardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));
        List<FreeBoardDTO> freeBoardDTOS = new ArrayList<>();
        for (FreeBoard freeBoard : freeBoards) {
            freeBoardDTOS.add(entityToDTO(freeBoard));
        }
        return freeBoardDTOS;
    }

    @Override // bno로 게시글 1개 찾아오기
    public FreeBoardDTO findFreeBoardById(Long bno, Integer mode) {
//        FreeBoard freeBoard = freeBoardRepository.findById(bno).orElse(null);
        FreeBoard freeBoard = freeBoardRepository.findByIdWithImages(bno).orElse(null);
        if(mode == 1){
            freeBoard.updateReadCount();
            freeBoardRepository.save(freeBoard);
        }
        FreeBoardDTO freeBoardDTO = entityToDTO(freeBoard);
        freeBoardDTO.setUsername(freeBoard.getMember().getUsername());

        return freeBoardDTO;
    }

    @Override // 게시글 업데이트
    public void updateFreeBoard(FreeBoardDTO freeBoardDTO) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardDTO.getBno()).orElse(null); // 기존의 board 데이터 가져오기
        freeBoard.change(freeBoardDTO.getTitle(), freeBoardDTO.getContent(), freeBoardDTO.getBtype());

        if(freeBoardDTO.getFreeFileDTOS() != null){
            freeBoard.removeFile();
            for(FreeFileDTO freeFileDTO : freeBoardDTO.getFreeFileDTOS()){
                freeBoard.addFile(freeFileDTO.getUuid(), freeFileDTO.getFilename(), freeFileDTO.isImage());
            }
        }
        freeBoardRepository.save(freeBoard);
    }

    @Override // 게시글 번호 삭제
    public void deleteFreeBoardById(Long bno) {
        FreeBoard freeBoard = freeBoardRepository.findByIdWithImages(bno).orElse(null);
        freeBoard.removeFile();
        freeBoardRepository.deleteById(bno);
    }

    @Override // 게시판 좋아요 버튼
    public FreeBoardDTO updateLikecount(Long bno) {
        FreeBoard freeBoard = freeBoardRepository.findById(bno).orElse(null);
        freeBoard.updateLikecount();
        freeBoardRepository.save(freeBoard);
        FreeBoardDTO dto = entityToDTO(freeBoard);
        return dto;
    }

    @Override
    public FreePageResponseDTO<FreeBoardDTO> getList(FreePageRequestDTO freePageRequestDTO) {
        Pageable pageable = freePageRequestDTO.getPageable("bno");
        Page<FreeBoard> result = freeBoardRepository.findAll(pageable);

            List<FreeBoardDTO> dtoList = result.getContent().stream()
                .map(freeBoard -> entityToDTO(freeBoard))
                .collect(Collectors.toList());
        int total = (int) result.getTotalElements(); // 객체의 레코드 수

        FreePageResponseDTO<FreeBoardDTO> freePageResponseDTO = FreePageResponseDTO.<FreeBoardDTO> withAll()
                .freePageRequestDTO(freePageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
        return freePageResponseDTO;
    }

    @Override
    public FreePageResponseDTO<FreeBoardListReplyCountDTO> getListReplyCount(FreePageRequestDTO freePageRequestDTO) {
        String[] types = freePageRequestDTO.getTypes();
        String keyword = freePageRequestDTO.getKeyword();

        Pageable pageable = freePageRequestDTO.getPageable("bno");
        Page<FreeBoardListReplyCountDTO> result = freeBoardRepository.searchWithReplyCount(types, keyword, pageable);

        return FreePageResponseDTO.<FreeBoardListReplyCountDTO> withAll()
                .freePageRequestDTO(freePageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<FreeBoardDTO> getTop10FreeBoardList() {
        List<FreeBoard> freeBoards = freeBoardRepository.findTop10ByOrderByBnoDesc();
        return freeBoards.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

}
