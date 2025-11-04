package com.four.animory.domain.mate;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_mate_reply")
@Getter
@Setter
@ToString(exclude = "mateBoard")

@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MateReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;

    @Column(nullable=false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="mid")
    private Member Member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private MateBoard mateBoard;

}
