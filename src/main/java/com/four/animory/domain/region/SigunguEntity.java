package com.four.animory.domain.region;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region_sigungu")
@Getter
@NoArgsConstructor
public class SigunguEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id", nullable = false)
    private SidoEntity sido;

    @Column(nullable = false, length = 45)
    private String name;
}