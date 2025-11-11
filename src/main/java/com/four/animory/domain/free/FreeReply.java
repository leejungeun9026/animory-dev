package com.four.animory.domain.free;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_free_reply")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"freeBoard", "member"})
public class FreeReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(nullable = false, length = 3000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private FreeBoard freeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_rno")
    private FreeReply parent;



}
