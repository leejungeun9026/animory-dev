package com.four.animory.service.user;

import com.four.animory.domain.user.Member;
import com.four.animory.domain.user.Pet;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.dto.user.MemberListPetCountDTO;
import com.four.animory.dto.user.PetDTO;
import com.four.animory.dto.user.UserRegisterDTO;

import java.util.List;

public interface UserService {
  int register(UserRegisterDTO userRegisterDTO);
  MemberDTO getMemberByUsername(String username);
  List<MemberListPetCountDTO>  getMemberListPetCount();
  boolean getSitterById(Long mid);
  void modifySitter(MemberDTO memberDTO);

  List<PetDTO> getPetListByMemberId(Long mid);


  default Member dtoToEntity(MemberDTO memberDTO){
    return Member.builder()
        .username(memberDTO.getUsername())
        .email(memberDTO.getEmail())
        .nickname(memberDTO.getNickname())
        .tel(memberDTO.getTel())
        .sido(memberDTO.getSido())
        .sigungu(memberDTO.getSigungu())
        .sitter(false)
        .role("USER")
        .build();
  }

  default MemberDTO entityToDTO(Member member){
    return MemberDTO.builder()
        .mid(member.getId())
        .username(member.getUsername())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .tel(member.getTel())
        .sido(member.getSido())
        .sigungu(member.getSigungu())
        .sitter(member.isSitter())
        .regDate(member.getRegDate())
        .updateDate(member.getUpdateDate())
        .build();
  }

  default Pet dtoToEntity(PetDTO petDTO){
    return Pet.builder()
        .type(petDTO.getType())
        .name(petDTO.getName())
        .age(petDTO.getAge())
        .build();
  }

  default PetDTO entityToDTO(Pet pet){
    return PetDTO.builder()
        .pid(pet.getId())
        .name(pet.getName())
        .age(pet.getAge())
        .type(pet.getType())
        .owner(pet.getMember().getNickname())
        .build();
  }
}
