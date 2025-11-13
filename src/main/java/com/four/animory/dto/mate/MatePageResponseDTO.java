package com.four.animory.dto.mate;

import com.four.animory.dto.common.PageRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MatePageResponseDTO<E> {
  private int page;   // 현재 페이지 번호
  private int size;   // 한 페이지당 표시할 리스트 개수
  private int total;  // 전체 데이터 개수
  private int totalReply;
  private int pageBlockSize; // 한 번에 표시할 페이지 번호 개수

  private int start;  // 페이지 블록 시작
  private int end;    // 페이지 블록 마지막
  private boolean prev; // 이전 블록 유무
  private boolean next; // 다음 블록 유무
  private int first;
  private int last;
  private List<E> dtoList;  // 실제 데이터 목록

  @Builder(builderMethodName = "withAll")
  public MatePageResponseDTO(MatePageRequestDTO matePageRequestDTO, List<E> dtoList, int total) {
    this.page = matePageRequestDTO.getPage();
    this.size = matePageRequestDTO.getSize();
    this.total = total;
    this.pageBlockSize = 3;
    this.dtoList = dtoList;

    this.first = 1;

    this.last = Math.max(1, (int) Math.ceil((double) total / this.size));

    int tempEnd = (int) (Math.ceil(this.page / (double) pageBlockSize) * pageBlockSize);
    this.start = tempEnd - (pageBlockSize - 1);
    this.end = Math.min(tempEnd, this.last);

    if (this.start < 1) this.start = 1;

    this.prev = this.start > 1;
    this.next = this.end < this.last;
  }
}
