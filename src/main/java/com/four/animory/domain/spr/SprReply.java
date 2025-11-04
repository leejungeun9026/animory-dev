package com.four.animory.domain.spr;


import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_spr_reply")
@Getter
@Setter
@ToString(exclude = "sprBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private SprBoard sprBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;
    private boolean secrete;
    private boolean deleted;
}
