package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.mate.QMateBoard;
import com.four.animory.domain.mate.QMateReply;
import com.four.animory.dto.mate.MateReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MateSearchImpl extends QuerydslRepositorySupport implements MateSearch {
    public MateSearchImpl() {
        super(MateBoard.class);
    }

    @Override
    public Page<MateBoard> searchAll(String[] types, String keyword, Pageable pageable) {
        QMateBoard qmateBoard = QMateBoard.mateBoard;
        JPQLQuery<MateBoard> query = from(qmateBoard);
        //키워드가 있을 경우 types 들어가고 없으면
        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) { //types 갯수만큼 for 돌아서
                switch (type) {
                    case "t":
                        builder.or(qmateBoard.title.containsIgnoreCase(keyword));
                        break;
                    case "c":
                        builder.or(qmateBoard.content.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        builder.or(qmateBoard.member.nickname.containsIgnoreCase(keyword));
                }
            }
            query.where(builder);
        }
        query.where(qmateBoard.bno.gt(0));
        //member를 가지고 와야지 실제 author를 넣을 수 있어
        this.getQuerydsl().applyPagination(pageable, query);
        List<MateBoard> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<MateReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QMateBoard qBoard = QMateBoard.mateBoard;
        QMateReply qreply = QMateReply.mateReply;
        JPQLQuery<MateBoard> query=from(qBoard);
        query.leftJoin(qreply).on(qreply.mateBoard.eq(qBoard)); //board 정보는 다 나오고(왼쪽), reply에 조건이 같은걸 붙혀
        //키워드가 있을 경우 types 들어가고 없으면,
        query.groupBy(qBoard); //리플이 여러개니까 그룹바이로 묶어

        //검색처리
        if(types!=null&&types.length>0 && keyword!=null){
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) { //types 갯수만큼 for 돌아서
                switch (type) {
                    case "t" :
                        builder.or(qBoard.title.containsIgnoreCase(keyword));
                        break;
                    case "c" :
                        builder.or(qBoard.content.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        builder.or(qBoard.member.nickname.containsIgnoreCase(keyword));
                }
            }
            query.where(builder);
        }
        query.where(qBoard.bno.gt(0));
//        select구문에서 필드를 구하는 것을 프로젝션, 행을 구하는 것은 where

        JPQLQuery<MateReplyCountDTO> dtoQuery=query.select(
                Projections.bean(MateReplyCountDTO.class,
                        qBoard.bno,
                        qBoard.title,
                        qBoard.member.username,
                        qBoard.member.nickname,
                        qBoard.category,
                        qBoard.sido,
                        qBoard.sigungu,
                        qBoard.readCount,
                        qBoard.regDate,
                        qBoard.likecount,
                        qBoard.dueDate,
                        qreply.count().as("replyCount")));

        //member를 가지고 와야지 실제 author를 넣을 수 있어
        this.getQuerydsl().applyPagination(pageable,query);
        List<MateReplyCountDTO> dtolist=dtoQuery.fetch();
        long count=query.fetchCount();
        return new PageImpl<>(dtolist,pageable,count);

    }
}
