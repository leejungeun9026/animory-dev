package com.four.animory.domain.free;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="tbl_free_board")
@Getter
@Setter
@ToString(exclude = {"member", "fileSet"})
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

    @Column(nullable = false, length = 20)
    private String btype;

    @ColumnDefault(value="0")
    private int readcount;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault(value="0")
    private int likecount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeReply> replies = new ArrayList<>(); // 실제 댓글 목록을 담는 필드

    @OneToMany(mappedBy = "freeBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size=20) // 한번에 20개씩 로딩하도록
    private Set<FreeFile> fileSet = new HashSet<>();  // 특정 게시물의 집합을 담을 변수

    // 게시판에 파일 추가
    public void addFile(String uuid, String filename, boolean image) {
        FreeFile freeFile = FreeFile.builder()
                .uuid(uuid)
                .filename(filename)
                .freeBoard(this)
                .image(image)
                .ord(fileSet.size())
                .build();
        fileSet.add(freeFile);
    }

    // 게시판에 파일 제거 + board와 관계 끊기
    public void removeFile(){
        fileSet.forEach(freeFile -> freeFile.changeFreeBoard(null));
        this.fileSet.clear();
    }

    public void change(String title, String content, String btype) {
        this.title = title;
        this.content = content;
        this.btype = btype;
    }
    public void updateReadCount() {
        this.readcount = readcount+1;
    }

    public void updateLikecount() { this.likecount = likecount+1; }

}
