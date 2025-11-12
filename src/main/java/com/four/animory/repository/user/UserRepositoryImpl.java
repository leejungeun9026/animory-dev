package com.four.animory.repository.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.QMember;
import com.four.animory.domain.user.QPet;
import com.four.animory.dto.user.MemberWithPetCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepository {
  public UserRepositoryImpl(){
    super(Member.class);
  }

  @Override
  public List<MemberWithPetCountDTO> findAllWithPetCount() {
    QMember qMember = QMember.member;
    QPet qPet = QPet.pet;

    JPQLQuery<MemberWithPetCountDTO> query = from(qMember)
        .leftJoin(qPet).on(qPet.member.eq(qMember))
        // ADMIN 제외
        .where(qMember.role.ne("ADMIN"))
        .groupBy(qMember.id)
        // 정렬 추가 (id DESC)
        .orderBy(qMember.id.desc())
        .select(Projections.bean(
            MemberWithPetCountDTO.class,
            qMember.id.as("mid"),
            qMember.username.as("username"),
            qMember.nickname.as("nickname"),
            qMember.email.as("email"),
            qMember.tel.as("tel"),
            qMember.sido.as("sido"),
            qMember.sigungu.as("sigungu"),
            qMember.sitter.as("sitter"),
            qMember.regDate.as("regDate"),
            qMember.updateDate.as("updateDate"),
            qPet.count().as("petCount")
        ));

    return query.fetch();
  }
}
