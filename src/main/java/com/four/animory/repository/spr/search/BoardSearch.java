package com.four.animory.repository.spr.search;

import com.four.animory.domain.spr.SprBoard;
import com.four.animory.dto.spr.SprBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<SprBoard> search1(Pageable pageable);
    Page<SprBoardDTO> searchAll(String[] types, String keyword, Pageable pageable);
}
