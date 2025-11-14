package com.four.animory.controller;

import com.four.animory.domain.region.Hospital;
import com.four.animory.dto.region.HospitalDTO;
import com.four.animory.repository.HospitalRepository;
import com.four.animory.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
public class HospitalController {
  private final HospitalService hospitalService;

  @GetMapping
  public List<HospitalDTO> getHospitalsInBounds(
      @RequestParam double minLng,
      @RequestParam double minLat,
      @RequestParam double maxLng,
      @RequestParam double maxLat) {

    // DB에 EPSG:5174 기준으로 lng(x), lat(y)가 저장되어 있다고 가정
    // 단순 between 조건으로 조회 (JPA 메소드 or QueryDSL)
    return hospitalService.findInBounds(minLng, maxLng, minLat, maxLat);
  }

}
