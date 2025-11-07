package com.four.animory.repository.user;

import com.four.animory.dto.user.MemberWithPetCountDTO;

import java.util.List;

public interface UserRepository {
  List<MemberWithPetCountDTO> findAllWithPetCount();
}
