package com.four.animory.service;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.Pet;
import com.four.animory.repository.user.MemberRepository;
import com.four.animory.repository.user.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Log4j2
public class UserServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void insertUserTest(){
        Member member = Member.builder()
                .username("test")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("테스트유저")
                .email("test@naver.com")
                .tel("010-1234-5678")
                .sido("부산광역시")
                .sigungu("부산진구")
                .role("USER")
                .sitter(false)
                .build();
        memberRepository.save(member);
        Member getMember = memberRepository.findByUsername(member.getUsername());
        Pet pet = Pet.builder()
                .type("dog")
                .name("행배")
                .age(2)
                .build();
        pet.setMember(getMember);
        petRepository.save(pet);
    }

    @Test
    public void insertAdminTest(){
        Member member = Member.builder()
                .username("admin1")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("관리자")
                .email("admin@naver.com")
                .tel("010-0000-1234")
                .sido("부산광역시")
                .sigungu("부산진구")
                .role("ADMIN")
                .sitter(false)
                .build();
        memberRepository.save(member);
    }
}
