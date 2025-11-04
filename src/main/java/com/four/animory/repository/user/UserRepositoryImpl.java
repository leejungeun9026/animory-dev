package com.four.animory.repository.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.QMember;
import com.four.animory.domain.user.QPet;
import com.four.animory.dto.user.MemberListPetCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepository {
  public UserRepositoryImpl(){
    super(Member.class);
  }

  @Override
  public List<MemberListPetCountDTO> findAllWithPetCount() {
    QMember qMember = QMember.member;
    QPet qPet = QPet.pet;

    JPQLQuery<MemberListPetCountDTO> query = from(qMember)
        .leftJoin(qPet).on(qPet.member.eq(qMember)) // Member 기준 LEFT JOIN
        .groupBy(qMember.id)
        .select(Projections.bean(
            MemberListPetCountDTO.class,
            qMember.id.as("mid"),
            qMember.username.as("username"),
            qMember.nickname.as("nickname"),
            qMember.email.as("email"),
            qMember.tel.as("tel"),
            qMember.sido.as("sido"),
            qMember.sigungu.as("sigungu"),
            qMember.sitter.as("sitter"),
            qMember.regDate.as("regDate"),
            qPet.count().as("petCount")
        ));

    List<MemberListPetCountDTO> result = query.fetch();
    return result;
  }
}
