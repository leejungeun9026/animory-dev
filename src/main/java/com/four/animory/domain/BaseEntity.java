package com.four.animory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value={AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {
  @CreationTimestamp
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Column(name="regdate", nullable = false)
  private LocalDateTime regDate;

  @UpdateTimestamp
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Column(name="updatedate")
  private LocalDateTime updateDate;
}
