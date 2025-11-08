package com.four.animory.repository.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.dto.free.FreeBoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreeSearch {
    Page<FreeBoard> search(Pageable pageable);
    Page<FreeBoard> searchAll(String[] types, String keyword, Pageable pageable);

    Page<FreeBoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
}
