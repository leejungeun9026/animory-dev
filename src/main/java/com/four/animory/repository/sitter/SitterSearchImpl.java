package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.QSitterBoard;
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

    QSitterBoard q = QSitterBoard.sitterBoard;

    // 1) 공통 조건 빌드
    BooleanBuilder where = new BooleanBuilder();
    where.and(q.bno.gt(0L));

    if (sido != null && !sido.isBlank()) {
      where.and(q.sido.eq(sido));
      if (sigungu != null && !sigungu.isBlank()) {
        where.and(q.sigungu.eq(sigungu));
      }
    }

    if (category != null && !category.isBlank()) {
      where.and(q.category.eq(category));
    }

    // state=n 이면 "완료" 포함 게시글 제외
    if ("n".equals(state)) {
      where.and(q.state.isNull().or(q.state.notLike("%완료%")));
    }

    if (petInfo != null && !petInfo.isBlank()) {
      where.and(q.petInfo.eq(petInfo));
    }

    if (field != null && field.length > 0 && keyword != null && !keyword.isBlank()) {
      BooleanBuilder kw = new BooleanBuilder();
      for (String f : field) {
        switch (f) {
          case "t":
            kw.or(q.title.containsIgnoreCase(keyword));
            break;
          case "c":
            kw.or(q.content.containsIgnoreCase(keyword));
            break; // ★ 빠져있던 break 추가
          case "w":
            kw.or(q.member.nickname.containsIgnoreCase(keyword)); // username이 아니라 nickname이면 여기 수정
            break;
        }
      }
      where.and(kw);
    }

    // 2) DTO 쿼리 (조건/정렬/페이징 모두 dtoQuery에 적용)
    JPQLQuery<SitterBoardListDTO> dtoQuery = from(q)
        .where(where)
        .select(Projections.bean(SitterBoardListDTO.class,
            q.bno,
            q.state,
            q.category,
            q.petInfo,
            q.sido,
            q.sigungu,
            q.title,
            q.member.nickname.as("nickname"),
            q.regDate,
            q.readCount
        ))
        .orderBy(q.regDate.desc());

    getQuerydsl().applyPagination(pageable, dtoQuery);

    List<SitterBoardListDTO> dtoList = dtoQuery.fetch();

    // 3) 카운트 쿼리 분리
    long total = from(q).where(where).select(q.count()).fetchOne();

    return new PageImpl<>(dtoList, pageable, total);
  }

}
