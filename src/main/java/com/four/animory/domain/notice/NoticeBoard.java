package com.four.animory.domain.notice;

import com.four.animory.domain.BaseEntity;

import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "noticeBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<NoticeFile> fileSet = new HashSet<>();


    public  void updateReadCount(){
        readCount = readCount+1;
    }
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void addFile(String uuid, String fileName, boolean image){
        NoticeFile noticeFile = NoticeFile.builder()
                .uuid(uuid)
                .filename(fileName)
                .noticeBoard(this)
                .ord(fileSet.size())
                .image(image)
                .build();
        fileSet.add(noticeFile);
    }

    public void removeFile(){
        fileSet.forEach(noticeFile  ->
                noticeFile.changeNoticeBoard(null));
        this.fileSet.clear();
    }

}
