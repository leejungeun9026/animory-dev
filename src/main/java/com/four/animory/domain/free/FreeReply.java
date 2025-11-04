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

    @Column(nullable=false, length = 500)
    private String content;
    @Column(nullable=false, length = 45)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private FreeBoard freeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

}
