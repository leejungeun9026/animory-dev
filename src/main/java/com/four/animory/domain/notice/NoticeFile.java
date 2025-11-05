package com.four.animory.domain.notice;

import com.four.animory.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_notice_file")
@Getter
@Setter
@ToString(exclude = "noticeBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeFile extends BaseEntity {

    @Id
    @Column(length = 30, nullable = false)
    private String uuid;

    @Column(nullable = false, length = 300)
    private String fileName;

    @Column(nullable = false)
    private boolean image;

    @Column(nullable = false)
    private int ord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno", nullable = false)
    private NoticeBoard noticeBoard;
}






