package com.four.animory.domain.sitter;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_sitter_file")
@Getter
@Setter
@ToString(exclude = "sitterBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SitterFile implements Comparable<SitterFile>{
  @Id
  private String uuid;
  private String filename;
  private int ord;
  private boolean image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name= "bno")
  private SitterBoard sitterBoard;

  public void changeBoard(SitterBoard sitterBoard) {
    this.sitterBoard = sitterBoard;
  }

  // OneToMany에서 순서에 맞게 정렬하기 위함
  @Override
  public int compareTo(SitterFile other) {
    return this.ord - other.ord;
  }
}
