package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateSearch {
  Page<MateBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
