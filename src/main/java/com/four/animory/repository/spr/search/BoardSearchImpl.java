package com.four.animory.repository.spr.search;

import com.four.animory.domain.spr.QSprBoard;
import com.four.animory.domain.spr.SprBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(SprBoard.class);
    }


    @Override
    public Page<SprBoard> search1(Pageable pageable) {
        QSprBoard qsprboard = QSprBoard.sprBoard;
        JPQLQuery<SprBoard> query = from(qsprboard);
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(qsprboard.title.containsIgnoreCase("a")); // 영문일 때 대소문자 구분 x + or title like '%a%'
        builder.or(qsprboard.content.containsIgnoreCase("a")); // or content like '%a%'
        builder.or(qsprboard.member.nickname.containsIgnoreCase("a")); // or author like '%a%'
        query.where(builder); // where
        query.where(qsprboard.bno.gt(0)); // and bno > 0
        this.getQuerydsl().applyPagination(pageable, query);
        List<SprBoard> list = query.fetch();
        long count = query.fetchCount();


        return new PageImpl<SprBoard>(list, pageable, count);
    }

    @Override
    public Page<SprBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        QSprBoard qsprboard = QSprBoard.sprBoard;
        JPQLQuery<SprBoard> query = from(qsprboard);
        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t" :
                        builder.or(qsprboard.title.containsIgnoreCase(keyword));
                        break;
                    case "c":
                        builder.or(qsprboard.content.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        builder.or(qsprboard.member.nickname.containsIgnoreCase(keyword));
                        break;
                    case "tc":
                        builder.or(qsprboard.title.containsIgnoreCase(keyword));
                        builder.or(qsprboard.content.containsIgnoreCase(keyword));
                        break;
                }
            }
            query.where(builder);
        }
        query.where(qsprboard.bno.gt(0));
        this.getQuerydsl().applyPagination(pageable, query);
        List<SprBoard> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }
}
