package com.four.animory.domain.notice;


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
public class NoticeFile implements Comparable<NoticeFile> {

    @Id
    private String uuid;
    private String filename;
    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private NoticeBoard noticeBoard;

    @Override
    public int compareTo(NoticeFile other) {
        return this.ord - other.ord;
    }

    public void changeNoticeBoard(NoticeBoard noticeBoard) {
        this.noticeBoard = noticeBoard;
    }
}






