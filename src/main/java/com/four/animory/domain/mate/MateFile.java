package com.four.animory.domain.mate;

import com.four.animory.domain.mate.MateBoard;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_mate_file")
@Getter
@Setter
@ToString(exclude = "mateBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateFile implements Comparable<MateFile> {

    @Id
    private String uuid;
    private String fileName;
    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private MateBoard mateBoard;

    // OneToMany에서 순서에 맞게 정렬하기 위함


    @Override
    public int compareTo(MateFile other) {
        return this.ord - other.ord;
    }
    // 게시판 파일이 바뀔 대
    public void changeMateBoard(MateBoard mateBoard) {
        this.mateBoard = mateBoard;
    }


}
