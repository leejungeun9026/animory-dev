package com.four.animory.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.dto.free.FreePageRequestDTO;
import com.four.animory.repository.free.FreeBoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class FreeBoardRepositoryTest {

    @Autowired
    private FreeBoardRepository freeBoardRepository;

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
}
