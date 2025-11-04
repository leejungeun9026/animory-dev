package com.four.animory.repository.user;

import com.four.animory.domain.user.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
  // 특정 회원의 펫 리스트
  @Query("SELECT p FROM Pet p WHERE p.member.id = :memberId")
  List<Pet> findPetsByMemberId(@Param("memberId") Long memberId);
}
