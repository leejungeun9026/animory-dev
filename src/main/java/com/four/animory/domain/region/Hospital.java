package com.four.animory.domain.region;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 200)
  private String name;
  @Column(nullable = false, length = 200)
  private String address;
  @Column(nullable = false, length = 200)
  private String roadname;
  @Column(length = 50)
  private String tel;
  @Column(length = 50)
  private String zip;
  @Column(nullable = false)
  private Double lng;
  @Column(nullable = false)
  private Double lat;
}
