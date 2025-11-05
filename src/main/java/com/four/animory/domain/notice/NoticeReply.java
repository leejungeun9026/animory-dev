package com.four.animory.domain.notice;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_notice_reply")
@Getter
@Setter
@ToString(exclude = {"board", "member"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno", nullable = false)
    private NoticeBoard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno", nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean deleted;

   
  // 게시판에서만 삭제되고 db에서는 삭제 안됨
    public void softDelete() {
        this.deleted = true;
    }
}
