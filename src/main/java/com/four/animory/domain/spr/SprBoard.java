package com.four.animory.domain.spr;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.sitter.SitterFile;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_spr_board")
@Getter
@Setter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false, length = 3000)
    private String content;
    private int readcount;
    @Column(name="duedate", nullable = true, length = 300)
    private String dueDate;
    private int recommend;
    private boolean complete;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid")
    private Member member;
    @Column(nullable = false)
    private String sido;
    @Column(nullable = false)
    private String sigungu;
//    @OneToMany(mappedBy = "sprBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    @BatchSize(size = 20)
//    private Set<SprFile> fileSet = new HashSet<>();

    public void change(String title, String content, Boolean complete){
        this.title = title;
        this.content = content;
        this.complete = complete;
    }
    public void updateReadCount(){
        this.readcount = this.readcount + 1;
    }
}
