package com.four.animory.domain.free;

import jakarta.persistence.*;
import lombok.*;
@Table(name = "tbl_free_file")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeFile {
    @Id
    private String uuid;
    private String filename;
    private boolean image;
    private int ord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private FreeBoard freeBoard;

}
