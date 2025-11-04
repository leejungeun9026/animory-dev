package com.four.animory.domain.user;

import com.four.animory.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_pet")
@Getter
@Setter
@ToString(exclude = "member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;
}
