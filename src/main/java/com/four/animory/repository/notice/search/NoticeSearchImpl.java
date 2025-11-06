package com.four.animory.repository.notice.search;

import com.four.animory.domain.notice.NoticeBoard;
import com.four.animory.domain.notice.QNoticeBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {
    public NoticeSearchImpl(){
        super(NoticeBoard.class);
    }

    @Override
    public Page<NoticeBoard> search1(Pageable pageable) {
        QNoticeBoard qnoticeBoard = QNoticeBoard.noticeBoard;
        JPQLQuery<NoticeBoard> query = from(qnoticeBoard);


        query.orderBy(qnoticeBoard.isPinned.desc(), qnoticeBoard.bno.desc());
        query.where(qnoticeBoard.bno.gt(0));

        this.getQuerydsl().applyPagination(pageable, query);
        List<NoticeBoard> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }



    @Override
    public Page<NoticeBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        QNoticeBoard qnoticeBoard = QNoticeBoard.noticeBoard;
        JPQLQuery<NoticeBoard> query = from(qnoticeBoard);

        if (types != null && types.length > 0 && keyword != null && keyword.length() > 0) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t" :
                        builder.or(qnoticeBoard.title.containsIgnoreCase(keyword));
                        break;
                    case "c" :
                        builder.or(qnoticeBoard.content.containsIgnoreCase(keyword));
                        break;
                    case "tc":
                        builder.or(qnoticeBoard.title.containsIgnoreCase(keyword));
                        builder.or(qnoticeBoard.content.containsIgnoreCase(keyword));
                        break;

                }
            }
            query.where(builder);
        }


        query.where(qnoticeBoard.bno.gt(0));
        // 핀 고정 유지 + 최신순
        query.orderBy(qnoticeBoard.isPinned.desc(), qnoticeBoard.bno.desc());

        this.getQuerydsl().applyPagination(pageable, query);
        List<NoticeBoard> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
