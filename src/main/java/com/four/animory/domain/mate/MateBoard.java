package com.four.animory.domain.mate;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_mate_board")
@Getter
@Setter
@ToString(exclude = {"member", "fileSet"})
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MateBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false, length = 45)
    private String state;
    @Column(nullable = false, length = 45)
    private String category;
    @Column(nullable = false, length = 45)
    private String petInfo;
    @Column(nullable = false, length = 45)
    private String sido;
    @Column(nullable = false, length = 45)
    private String sigungu;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;

   @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="mid") //fat info
    private Member member;

    @ColumnDefault(value="0")
    private int readCount;

//    @OneToMany(mappedBy = "sitterBoard", fetch = FetchType.LAZY, cascae = CascadeType.ALL)
//    private Set<MateFile> fileSet = new HashSet<>();

}
