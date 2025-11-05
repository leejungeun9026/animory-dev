package com.four.animory.dto.sitter;

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
public class SitterBoardPageRequestDTO {
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
  private String state;
  private String petInfo;
  private String field;
  private String keyword;

  public String[] getFields(){
    if(field == null){
      return null;
    } else {
      return field.split("");
    }
  }
  
  // Pageable객체 요청 시 page 시작번호 보정, 정렬기준 및 정렬방법 설정
  public Pageable getPageable(String...props){
    return PageRequest.of(page-1, size, Sort.by(props).descending());
  }

  public String getLink(){
    if (link == null){
      StringBuilder builder = new StringBuilder();
      builder.append("page="+this.page);
      builder.append("&size="+this.size);
      builder.append("&sido="+this.sido);
      builder.append("&sigungu="+this.sigungu);
      builder.append("&category="+this.category);
      builder.append("&status="+this.state);
      builder.append("&petInfo="+this.petInfo);
      if(field!=null && !field.isEmpty() && keyword!= null) {
        builder.append("&field="+this.field);
      }
      if(keyword!=null) {
        try {
          builder.append("&keyword="+ URLEncoder.encode(this.keyword, "UTF-8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      link = builder.toString();
    }
    return link;
  }
}
