package com.four.animory.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private Long mid;
    @NotEmpty
    String username;
    @NotEmpty
    String password;
    @NotEmpty
    String nickname;
    @NotEmpty
    String tel;
    @NotEmpty
    String email;
    @NotEmpty
    String sido;
    @NotEmpty
    String sigungu;
    boolean sitter;
    LocalDateTime regDate;
    LocalDateTime updateDate;
}
