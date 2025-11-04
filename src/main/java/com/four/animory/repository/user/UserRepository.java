package com.four.animory.repository.user;

import com.four.animory.dto.user.MemberListPetCountDTO;

import java.util.List;

public interface UserRepository {
  List<MemberListPetCountDTO> findAllWithPetCount();
}
