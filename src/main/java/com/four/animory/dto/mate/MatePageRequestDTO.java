package com.four.animory.dto.mate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatePageRequestDTO {
  @Builder.Default
  @Min(value = 1)
  @Positive
  private int page = 1;

  @Builder.Default
  @Min(value = 10)
  @Max(value = 100)
  @Positive
  private int size = 10;
  private String link;
  private String sido;
  private String sigungu;
  private String category;
  private String field;
  private String keyword;
  private String type;

    public String[] getTypes(){
        if(type==null || type.isEmpty()){
            return null;
        } else {
            return type.split("");
        }
    }
  
  // Pageable객체 요청 시 page 시작번호 보정, 정렬기준 및 정렬방법 설정
  public Pageable getPageable(String...props){
    return PageRequest.of(page-1, size, Sort.by(props).descending());
  }

  public String getLink(){
    if (link == null){
      StringBuilder builder = new StringBuilder();
      builder.append("page=").append(this.page);
      builder.append("&size=").append(this.size);

      // null 또는 빈 문자열은 제외
      if (this.sido != null && !this.sido.isEmpty()) {
        builder.append("&sido=").append(this.sido);
      }
      if (this.sigungu != null && !this.sigungu.isEmpty()) {
        builder.append("&sigungu=").append(this.sigungu);
      }
      if (this.category != null && !this.category.isEmpty()) {
        builder.append("&category=").append(this.category);
      }
      // 검색 필드와 키워드가 있을 경우만 추가
      if (this.field != null && !this.field.isEmpty() && this.keyword != null && !this.keyword.isEmpty()) {
        builder.append("&field=").append(this.field);
      }
      if (this.keyword != null && !this.keyword.isEmpty()) {
        try {
          builder.append("&keyword=").append(URLEncoder.encode(this.keyword, "UTF-8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      link = builder.toString();
    }
    return link;
  }
}
