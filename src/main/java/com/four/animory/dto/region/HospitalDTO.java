package com.four.animory.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDTO {
  private Long id;
  private String name;
  private String address;
  private String roadname;
  private String tel;
  private String zip;
  private Double lng;
  private Double lat;
}
