package com.four.animory.service.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.Pet;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.dto.user.MemberListPetCountDTO;
import com.four.animory.dto.user.PetDTO;
import com.four.animory.dto.user.UserRegisterDTO;
import com.four.animory.repository.user.MemberRepository;
import com.four.animory.repository.user.PetRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PetRepository petRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Transactional
  public int register(UserRegisterDTO userRegisterDTO){
    MemberDTO memberDTO = userRegisterDTO.getMember();
    Member member = dtoToEntity(memberDTO);
    member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
    memberRepository.save(member);

    // 펫 정보 있는 경우
    if (userRegisterDTO.getPets() != null){
      for (PetDTO petDTO : userRegisterDTO.getPets()) {
        Pet pet = dtoToEntity(petDTO);
        pet.setMember(member);
        petRepository.save(pet);
      }
    }

    Member result = memberRepository.findByUsername(member.getUsername());
    log.info("userService Result : " + result);
    if (result != null){
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public MemberDTO getMemberById(Long mid) {
    Member member = memberRepository.findById(mid).orElse(null);
    return entityToDTO(Objects.requireNonNull(member));
  }

  @Override
  public MemberDTO getMemberByUsername(String username) {
      return entityToDTO(memberRepository.findByUsername(username));
  }


  @Override
  public List<MemberListPetCountDTO> getMemberListPetCount() {
    return memberRepository.findAllWithPetCount();
  }

  @Override
  public boolean getSitterById(Long mid) {
    return memberRepository.findSitterById(mid);
  }

  @Override
  public void modifySitter(MemberDTO memberDTO) {
    Member member = memberRepository.findById(memberDTO.getMid())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    member.setSitter(memberDTO.isSitter());
    memberRepository.save(member);
  }

  @Override
  public List<PetDTO> getPetListByMemberId(Long mid) {
    List<Pet> petList = petRepository.findPetsByMemberId(mid);
    List<PetDTO> petDTOs = new ArrayList<>();
    for (Pet pet : petList) {
      petDTOs.add(entityToDTO(pet));
    }
    return petDTOs;
  }
}
