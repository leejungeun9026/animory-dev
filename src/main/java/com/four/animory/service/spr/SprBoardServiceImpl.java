package com.four.animory.service.spr;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.dto.spr.SprFileDTO;
import com.four.animory.repository.spr.SprReplyRepository;
import com.four.animory.repository.spr.SprSprBoardRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SprBoardServiceImpl implements SprBoardService {
    @Autowired
    private SprSprBoardRepository sprRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SprReplyRepository sprReplyRepository;

    @Override
    public void registerSprBoard(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = dtoToEntity(sprBoardDTO);
        Member member = memberRepository.findByUsername(sprBoardDTO.getAuthor());
        sprBoard.setMember(member);
        sprRepository.save(sprBoard);
    }

    @Override
    public PageResponseDTO<SprBoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<SprBoardDTO> result =  sprRepository.searchAll(
                pageRequestDTO.getTypes(), pageRequestDTO.getKeyword(), pageable);
        PageResponseDTO<SprBoardDTO> responseDTO = PageResponseDTO.<SprBoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
        return responseDTO;
    }

    @Override
    public SprBoardDTO findBoardById(Long bno, int mode) {
        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        if(mode==1){
            sprBoard.updateReadCount();
            sprRepository.save(sprBoard);
        }
        SprBoardDTO dto = entityToDTO(sprBoard);
        return dto;
    }

    @Override
    public void updateBoard(SprBoardDTO sprBoardDTO) {
        SprBoard sprBoard = sprRepository.findById(sprBoardDTO.getBno()).orElse(null);
        sprBoard.change(sprBoardDTO.getTitle(), sprBoardDTO.getContent(), sprBoardDTO.isComplete());
        if(sprBoardDTO.getSprFileDTOs() != null){
            sprBoard.removeFile();
            for(SprFileDTO fileDTO : sprBoardDTO.getSprFileDTOs()){
                sprBoard.addFile(fileDTO.getUuid(), fileDTO.getFileName(), fileDTO.isImage());
            }
        }
        sprRepository.save(sprBoard);
    }

    @Override
    public void deleteBoardById(Long bno) {
        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        sprBoard.removeFile();
        sprReplyRepository.deleteBySprBoardId(bno);
        sprRepository.deleteById(bno);
    }

    @Override
    public SprBoardDTO updateRecommend(Long bno) {
        SprBoard sprBoard = sprRepository.findById(bno).orElse(null);
        sprBoard.updateRecommend();
        sprRepository.save(sprBoard);
        SprBoardDTO dto = entityToDTO(sprBoard);
        return dto;
    }

    @Override
    public List<SprBoardDTO> getTop10SprBoards() {
        List<SprBoard> sprBoards = sprRepository.findTop10ByOrderByRecommendDesc();
        return sprBoards.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


}
