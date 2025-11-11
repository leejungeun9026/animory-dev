package com.four.animory.service.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.Pet;
import com.four.animory.dto.user.*;
import com.four.animory.repository.user.MemberRepository;
import com.four.animory.repository.user.PetRepository;
import com.four.animory.service.sitter.SitterBoardService;
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
  @Autowired
  private SitterBoardService sitterBoardService;

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
  public List<MemberWithPetCountDTO> getMemberListPetCount() {
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

  @Override
  public void updatePetList(PetListDTO petListDTO) {
    Long mid = petListDTO.getMid();
    List<PetDTO> petDTOs = petListDTO.getPetDTO();
    log.info("petListDTO"+petListDTO);
    log.info("mid"+mid);
    log.info("petDTOs"+petDTOs);

    Member member = memberRepository.findById(mid).orElse(null);
    List<Pet> petList = petRepository.findPetsByMemberId(mid);
    log.info(member);
    log.info(petList);

    // DB에 있는 petList와 입력한 petDTOList 비교
    for(Pet pet : petList){
      log.info("for문 시작" + pet);
      boolean isSame = false;

      for(PetDTO petDTO : petDTOs){
        log.info("이중for문 시작 DTO");
        if(pet.getId().equals(petDTO.getPid())){
          // 내가 입력한 petID와 DB에 있는 petID가 일치하면 업데이트
          Pet updatePet = petRepository.findById(pet.getId()).orElse(null);
          updatePet.change(petDTO.getName(), petDTO.getAge(), petDTO.getType());
          log.info("수정");
          petRepository.save(updatePet);
          isSame = true;
        }
      }
      if(isSame){
        // 같으면 업데이트 수행하고 왔으므로 아무일도 일어나지 않음
      } else {
        // 다르면(DB에 없으면) 삭제
        log.info("삭제");
        petRepository.deleteById(pet.getId());
      }
    }

    // 입력한 petDTO에 petID가 없으면
    for(PetDTO petDTO : petDTOs){
      if(petDTO.getPid() == null){
        Pet insertPet = dtoToEntity(petDTO);
        insertPet.setMember(member);
        log.info("등록");
        petRepository.save(insertPet);
      }
    }
  }
}
