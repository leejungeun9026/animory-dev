package com.four.animory.domain.mate;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.mate.MateFile;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
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
    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 45)
    private String sido;
    @Column(nullable = false, length = 45)
    private String sigungu;
    @Column(nullable = false, length = 300)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    private boolean complete;
    @ColumnDefault(value="0")
    private int readCount;
    @Column(name="duedate", nullable = true, length = 300)
    private String dueDate;


    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="mid") //fat info
    private Member member;

    public void change(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void updateReadCount(){ this.readCount = this.readCount + 1; }

    @OneToMany(mappedBy = "mateBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<MateFile> fileSet = new HashSet<>();
    public void addFile(String uuid, String fileName, boolean image){
        MateFile mateFile = MateFile.builder()
                .uuid(uuid)
                .fileName(fileName)
                .mateBoard(this)
                .ord(fileSet.size())
                .image(image)
                .build();
        fileSet.add(mateFile);
    }

    public void removeFile(){
        fileSet.forEach(mateFile ->
                mateFile.changeMateBoard(null));
        this.fileSet.clear();
    }



}
