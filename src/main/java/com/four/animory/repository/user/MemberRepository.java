package com.four.animory.repository.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.Pet;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer>, UserRepository {
  Member findByUsername(String username);

  @Query("SELECT m.sitter FROM Member m WHERE m.id = :memberId")
  boolean findSitterById(@RequestParam Long memberId);

  Member findMemberById(Long mid);
}
