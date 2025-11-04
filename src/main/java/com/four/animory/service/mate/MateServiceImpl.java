package com.four.animory.service.mate;


import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.repository.mate.MateRepository;
import com.four.animory.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MateServiceImpl implements MateService {
    @Autowired
    private MateRepository mateRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Override
    public void regesiterMateBoard(MateBoardDTO mateBoardDTO) {
        MateBoard board = dtoToEntity(mateBoardDTO);
        Member member = memberRepository.findByUsername("test");
        board.setMember(member);
        mateRepository.save(board);

    }
}
