package com.four.animory.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.repository.free.FreeBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FreeBoardTest {
    @Autowired
    FreeBoardRepository freeBoardRepository;

    @Test
    void insertFreeBoard() {
        FreeBoard freeBoard = FreeBoard.builder()
                .title("제목5")
                .content("내용5")
                .username("유저5")
                .nickname("닉네임5")
                .btype("자유")
                .build();
        freeBoardRepository.save(freeBoard);
    }
}
