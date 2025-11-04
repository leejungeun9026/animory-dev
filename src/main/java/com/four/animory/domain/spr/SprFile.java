package com.four.animory.domain.spr;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_spr_file")
@Getter
@Setter
@ToString(exclude = "sprboard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprFile implements Comparable<SprFile> {
    @Id
    private String uuid;
    private String fileName;
    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private SprBoard sprboard;

    @Override
    public int compareTo(SprFile other) {
        return this.ord - other.ord;
    }
}
