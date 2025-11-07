package com.four.animory.repository.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.QFreeBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class FreeSearchImpl extends QuerydslRepositorySupport implements FreeSearch {
    public FreeSearchImpl() {
        super(FreeBoard.class);
    }

    @Override
    public Page<FreeBoard> search(Pageable pageable) {
        QFreeBoard qFreeBoard = QFreeBoard.freeBoard;

        JPQLQuery<FreeBoard> query=from(qFreeBoard);
        BooleanBuilder builder=new BooleanBuilder(); //where절 적어줌.
        builder.or(qFreeBoard.title.containsIgnoreCase("제목"));
        //containsIgnoreCase 영문일 때 대소문자 구분X
        // or title like '%1%'

        builder.or(qFreeBoard.content.containsIgnoreCase("내용"));
        // or content like '%1%'

        builder.or(qFreeBoard.member.username.containsIgnoreCase("abc"));
        query.where(builder); // build값을 넣어서 where절을 만들라는 뜻

// where title like '%=%' or content like '%=%' of author like '%=%' and bno>0 limit 0,5

        query.where(qFreeBoard.bno.gt(0));
        // greater than 0 : bno가 0보다 큰 것. index에서 먼저 찾기 때문에 시간을 줄여줌.
        this.getQuerydsl().applyPagination(pageable, query);
        List<FreeBoard> list=query.fetch();
        // fetch() -> query로부터 검색한 데이터 전부를 가져온다.
        long count = query.fetchCount();
        // fetchCount() 전체 레코드 수
        return new PageImpl<FreeBoard>(list,pageable,count);
        //PageImpl 객체를 만들어서 리턴함.
    }

    @Override
    public Page<FreeBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        return null;
    }
}
