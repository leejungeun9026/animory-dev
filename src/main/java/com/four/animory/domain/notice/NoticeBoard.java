package com.four.animory.domain.notice;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_notice_board")
public class NoticeBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    @ColumnDefault(value="0")
    private int readCount;
    @Column(nullable = false)
    private boolean isPinned;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    public  void updateReadCount(){
        readCount = readCount+1;
    }
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
