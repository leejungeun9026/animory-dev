package com.four.animory.domain.sitter;

import com.four.animory.domain.BaseEntity;
import com.four.animory.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="tbl_sitter_reply")
@Getter
@Setter
@ToString(exclude = {"member", "sitterBoard"})
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
  @Column(name = "hiring", nullable = false)
  @ColumnDefault(value="0")
  private boolean isHiring;
}
