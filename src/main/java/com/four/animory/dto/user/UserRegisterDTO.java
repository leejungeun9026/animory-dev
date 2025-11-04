package com.four.animory.dto.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRegisterDTO {
    private MemberDTO member;
    private List<PetDTO> pets = new ArrayList<>();
}
