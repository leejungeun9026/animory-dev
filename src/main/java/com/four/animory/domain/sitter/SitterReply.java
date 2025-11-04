package com.four.animory.domain.sitter;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_sitter_reply")
@Getter
@Setter
@ToString(exclude = "sitterBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SitterReply extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;
  @Column(nullable = false, length = 1000)
  private String content;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="mid")
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bno")
  private SitterBoard sitterBoard;
}
