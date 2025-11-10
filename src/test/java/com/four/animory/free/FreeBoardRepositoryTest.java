package com.four.animory.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.FreeReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardListReplyCountDTO;
import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.repository.free.FreeBoardRepository;
import com.four.animory.repository.free.FreeReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class FreeBoardRepositoryTest {

    @Autowired
    private FreeBoardRepository freeBoardRepository;
    @Autowired
    private FreeReplyRepository freeReplyRepository;
    @Autowired
    private PersistenceManagedTypes persistenceManagedTypes;

    @Test
    public void testSearch(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        freeBoardRepository.search(pageable);
    }
    @Test
    public void testSearchAll(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<FreeBoard> result = freeBoardRepository.searchAll(types, keyword, pageable);
    }
    @Transactional
    @Test
    public void testSearchAllByCategory(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 5, Sort.by("bno").descending());
        Page<FreeBoard> result = freeBoardRepository.searchAll(types, keyword, pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+":"+result.hasNext());
        result.getContent().forEach(freeBoard -> log.info(freeBoard));
    }

    @Transactional
    @Test
    @Rollback(false)
    public void testInsert(){
        Long bno=28L;
        Long mno=3L;

        FreeBoard freeBoard = FreeBoard.builder().bno(bno).build();
        Member member = Member.builder().id(3L).build();

        FreeReply freeReply = FreeReply.builder()
                .freeBoard(freeBoard)
                .member(member)
                .content("댓글 연습")
                .nickname("냥냥")
                .username("유저3")
                .build();
        freeReplyRepository.save(freeReply);
    }

    @Test
    public void testBoardReplies(){
        Long bno=28L;
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rno").descending());
        Page<FreeReply> result = freeReplyRepository.listOfBoard(bno,pageable);
        result.getContent().forEach(freeReply -> log.info(freeReply));
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<FreeBoardListReplyCountDTO> result = freeBoardRepository.searchWithReplyCount(types, keyword, pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+":"+result.hasNext());
        result.getContent().forEach(freeBoard -> log.info(freeBoard));
    }
}
