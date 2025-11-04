package com.four.animory.domain.user;

import com.four.animory.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user_member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false, length = 30)
    String nickname;
    @Column(nullable = false, length = 13, unique = true)
    String tel;
    @Column(nullable = false, length = 120, unique = true)
    String email;
    @Column(nullable = false, length = 45)
    String sido;
    @Column(nullable = false, length = 45)
    String sigungu;
    @Column(nullable = false, length = 10)
    String role;
    @ColumnDefault(value="0")
    boolean sitter;
}
