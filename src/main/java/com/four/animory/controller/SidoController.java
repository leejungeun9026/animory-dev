package com.four.animory.controller;

import com.four.animory.dto.region.SidoDTO;
import com.four.animory.dto.region.SigunguDTO;
import com.four.animory.repository.region.SidoRepository;
import com.four.animory.repository.region.SigunguRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class SidoController {
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;

    @GetMapping("/sido")
    public List<SidoDTO> getSidos() {
        return sidoRepository.findAll(Sort.by("name").ascending())
                .stream().map(s -> new SidoDTO(s.getId(), s.getName()))
                .toList();
    }

    @GetMapping("/sigungu")
    public List<SigunguDTO> getSigungu(@RequestParam Long sidoId) {
        return sigunguRepository.findBySido_IdOrderByNameAsc(sidoId)
                .stream().map(g -> new SigunguDTO(g.getId(), g.getName(), g.getSido().getId()))
                .toList();
    }
}
