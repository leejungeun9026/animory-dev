package com.four.animory.repository.free;

import com.four.animory.domain.free.FreeBoard;
import com.four.animory.domain.free.QFreeBoard;
import com.four.animory.domain.free.QFreeReply;
import com.four.animory.dto.free.FreeBoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
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
        builder.or(qFreeBoard.content.containsIgnoreCase("내용"));
        builder.or(qFreeBoard.member.username.containsIgnoreCase("abc"));
        query.where(builder); // build값을 넣어서 where절을 만들라는 뜻

        query.where(qFreeBoard.bno.gt(0)); // bno가 0보다 큰 글만
        this.getQuerydsl().applyPagination(pageable, query); // 페이지 번호랑 한 페이지당 몇 개 글 보여줄건지
        List<FreeBoard> list=query.fetch();
        long count = query.fetchCount(); // 전체 글이 몇 페이지인지
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public Page<FreeBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        QFreeBoard qFreeBoard = QFreeBoard.freeBoard;
        JPQLQuery<FreeBoard> query=from(qFreeBoard);

        if((types!=null && types.length>0) && keyword!=null) {
            BooleanBuilder booleanBuilder=new BooleanBuilder();
            for(String type:types){
                switch (type){
                    case "t":
                        booleanBuilder.or(qFreeBoard.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(qFreeBoard.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(qFreeBoard.member.username.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(qFreeBoard.bno.gt(0));
        this.getQuerydsl().applyPagination(pageable, query);
        List<FreeBoard> list=query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public Page<FreeBoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QFreeBoard qFreeBoard = QFreeBoard.freeBoard;
        QFreeReply qFreeReply = QFreeReply.freeReply;

        JPQLQuery<FreeBoard> query=from(qFreeBoard);
        query.leftJoin(qFreeReply).on(qFreeReply.freeBoard.eq(qFreeBoard));
        query.groupBy(qFreeBoard);

        if(types!=null && types.length>0 && keyword!=null) {
            BooleanBuilder booleanBuilder=new BooleanBuilder();
            for(String type:types){
                switch (type){
                    case "t":
                        booleanBuilder.or(qFreeBoard.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(qFreeBoard.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(qFreeBoard.member.username.contains(keyword));
                        break;
                }
            } query.where(booleanBuilder);
        }
        query.where(qFreeBoard.bno.gt(0));
        JPQLQuery<FreeBoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(FreeBoardListReplyCountDTO.class,
            qFreeBoard.bno,
                qFreeBoard.title,
                qFreeBoard.member.username.as("username"),
                qFreeBoard.readcount,
                qFreeBoard.regDate,
                qFreeReply.count().as("replycount")));
        this.getQuerydsl().applyPagination(pageable, query);
        List<FreeBoardListReplyCountDTO> dtoList=dtoQuery.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(dtoList,pageable,count);
    }
}
