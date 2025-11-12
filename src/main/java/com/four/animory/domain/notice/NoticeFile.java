package com.four.animory.domain.notice;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notice_file")
@Getter
@Setter
@ToString(exclude = "noticeBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeFile implements Comparable<NoticeFile> {

    @Id
    private String uuid;
    @Column(name = "file_name")
    private String fileName;

    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private NoticeBoard noticeBoard;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regdate;

    @Override
    public int compareTo(NoticeFile other) {
        return this.ord - other.ord;
    }

    public void changeNoticeBoard(NoticeBoard noticeBoard) {
        this.noticeBoard = noticeBoard;
    }
}






