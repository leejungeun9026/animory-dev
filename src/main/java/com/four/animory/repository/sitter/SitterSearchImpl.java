package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.QSitterBoard;
import com.four.animory.domain.sitter.QSitterReply;
import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.dto.sitter.SitterBoardListDTO;
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
public class SitterSearchImpl extends QuerydslRepositorySupport implements SitterSearch {
  public SitterSearchImpl() {
    super(SitterBoard.class);
  }
  @Override
  public Page<SitterBoardListDTO> search(
      String sido, String sigungu, String category, String state,
      String petInfo, String[] field, String keyword, Pageable pageable) {

    QSitterBoard qBoard = QSitterBoard.sitterBoard;
    QSitterReply qReply = QSitterReply.sitterReply;

    // 1) 공통 조건 빌드
    BooleanBuilder where = new BooleanBuilder();
    where.and(qBoard.bno.gt(0L));

    if (sido != null && !sido.isBlank()) {
      where.and(qBoard.sido.eq(sido));
      if (sigungu != null && !sigungu.isBlank()) {
        where.and(qBoard.sigungu.eq(sigungu));
      }
    }

    if (category != null && !category.isBlank()) {
      where.and(qBoard.category.eq(category));
    }

    // state=n 이면 "완료" 포함 게시글 제외
    if ("n".equals(state)) {
      where.and(qBoard.state.isNull().or(qBoard.state.notLike("%완료%")));
    }

    if (petInfo != null && !petInfo.isBlank()) {
      where.and(qBoard.petInfo.eq(petInfo));
    }

    if (field != null && field.length > 0 && keyword != null && !keyword.isBlank()) {
      BooleanBuilder kw = new BooleanBuilder();
      for (String f : field) {
        switch (f) {
          case "t":
            kw.or(qBoard.title.containsIgnoreCase(keyword));
            break;
          case "c":
            kw.or(qBoard.content.containsIgnoreCase(keyword));
            break;
          case "w":
            kw.or(qBoard.member.nickname.containsIgnoreCase(keyword));
            break;
        }
      }
      where.and(kw);
    }

    // 2) DTO 쿼리 (조건/정렬/페이징 모두 dtoQuery에 적용)
    JPQLQuery<SitterBoardListDTO> dtoQuery = from(qBoard)
        .leftJoin(qReply).on(qReply.sitterBoard.eq(qBoard))
        .where(where)
        .groupBy(
            qBoard.bno, qBoard.state, qBoard.category, qBoard.petInfo,
            qBoard.sido, qBoard.sigungu, qBoard.title, qBoard.member.nickname,
            qBoard.regDate, qBoard.readCount
        )
        .select(Projections.bean(SitterBoardListDTO.class,
            qBoard.bno,
            qBoard.state,
            qBoard.category,
            qBoard.petInfo,
            qBoard.sido,
            qBoard.sigungu,
            qBoard.title,
            qBoard.member.nickname.as("nickname"),
            qBoard.regDate,
            qBoard.readCount,
            qReply.count().as("replyCount")
        ));

    this.getQuerydsl().applyPagination(pageable, dtoQuery);

    List<SitterBoardListDTO> dtoList = dtoQuery.fetch();
    Long total = dtoQuery.fetchCount();
    if (total == null ) { total = 0L; };

    return new PageImpl<>(dtoList, pageable, total);
  }
}
