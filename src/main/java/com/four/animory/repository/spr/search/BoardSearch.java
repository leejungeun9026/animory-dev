package com.four.animory.repository.spr.search;

import com.four.animory.domain.spr.SprBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<SprBoard> search1(Pageable pageable);
    Page<SprBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
