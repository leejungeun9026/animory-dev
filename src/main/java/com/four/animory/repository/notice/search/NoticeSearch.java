package com.four.animory.repository.notice.search;

import com.four.animory.domain.notice.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeSearch {
    Page<NoticeBoard> search1(Pageable pageable);
    Page<NoticeBoard> searchAll(String[] types, String keyword, Pageable pageable);

}
