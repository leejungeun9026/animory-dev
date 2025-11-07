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

public class MateBoardPageRequestDTO {
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
    private String type;
    private String keyword;

    public String[] getTypes() {
        if (type == null || type.isEmpty()) { //없으면  null
            return null;
        }
        return type.split("");
    }

    // Pageable객체 요청 시 page 시작번호 보정, 정렬기준 및 정렬방법 설정
    public Pageable getPageable(String... props) {
        return PageRequest.of(page - 1, size, Sort.by(props).descending());
    }

    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);
            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }
            if (keyword != null && keyword.length() > 0) {
                builder.append("&keyword=" + keyword);
            }
            link = builder.toString();
        }
        return link;
    }

}