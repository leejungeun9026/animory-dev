package com.four.animory.domain.free;

import jakarta.persistence.*;
import lombok.*;
@Table(name = "tbl_free_file")
@Entity
@Getter
@Setter
@ToString(exclude = "freeBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeFile implements Comparable<FreeFile>{
    @Id
    private String uuid;
    private String filename;
    private int ord; // 파일 순서
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private FreeBoard freeBoard;

    // 파일의 순서 정하기
    @Override
    public int compareTo(FreeFile other) {
        return this.ord - other.ord;
    }

    // 게시판 파일이 바뀔 대
    public void changeFreeBoard(FreeBoard freeBoard) {
        this.freeBoard = freeBoard;
    }

}
