package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.domain.mate.QMateBoard;
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


//        JPQLQuery<MateReplyCountDTO> dtoQuery=query.select(
//                Projections.bean(MateReplyCountDTO.class,
//                        qmateBoard.bno,
//                        qmateBoard.title,
//                        qmateBoard.member.username,
//                        qmateBoard.readCount,
//                        qmateBoard.regDate));
////                        qmatereply.count().as("replyCount")));

        //member를 가지고 와야지 실제 author를 넣을 수 있어
        this.getQuerydsl().applyPagination(pageable, query);
        List<MateBoard> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);

    }
}
