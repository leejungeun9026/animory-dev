package com.four.animory.domain.spr;


import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_spr_reply")
@Getter
@Setter
@ToString(exclude = {"sprBoard","parent","children"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    @Column(nullable = false, length = 3000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private SprBoard sprBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;
    private boolean secret;
    private boolean deleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_rno")
    private SprReply parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<SprReply> children = new ArrayList<>();
}
