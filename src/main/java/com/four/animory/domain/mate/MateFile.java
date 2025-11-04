package com.four.animory.domain.mate;

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
    private String filename;
    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    private MateBoard mateBoard;

    public void changeBoard(MateBoard mateBoard) {
        this.mateBoard = mateBoard;
    }

    // OneToMany에서 순서에 맞게 정렬하기 위함
    @Override
    public int compareTo(MateFile other) {
        return this.ord - other.ord;
    }



}
