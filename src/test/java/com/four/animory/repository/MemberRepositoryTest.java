package com.four.animory.repository;

import com.four.animory.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class MemberRepositoryTest {
  @Autowired
  MemberRepository memberRepository;

  @Test
  public void getListPetCountTest() {
    memberRepository.findAllWithPetCount();
    log.info(memberRepository.findAllWithPetCount());
  }

  @Test
  public void getSitterByIdTest(){
    log.info(memberRepository.findSitterById(4L));
  }
}
