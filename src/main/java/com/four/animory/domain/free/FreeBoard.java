package com.four.animory.domain.free;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_free_board")
@Getter
@Setter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    @Column(nullable = false, length = 45)
    private String writer;
    @Column(nullable = false, length = 20)
    private String btype;

    @ColumnDefault(value="0")
    private int readcount;

    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private int likecount =0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeReply> replies = new ArrayList<>(); // 실제 댓글 목록을 담는 필드

}
