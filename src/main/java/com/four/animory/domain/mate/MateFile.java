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

    @Override
    public int compareTo(MateFile other) {
        return this.ord - other.ord;
    }

    public void changeMateBoard(MateBoard mateBoard) {
        this.mateBoard = mateBoard;
    }


}
