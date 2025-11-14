package com.four.animory.service;

import com.four.animory.domain.region.Hospital;
import com.four.animory.dto.region.HospitalDTO;
import com.four.animory.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HospitalService {
  public List<HospitalDTO> findInBounds(double minLng, double maxLng, double minLat, double maxLat);

  default HospitalDTO entityToDTO(Hospital hospital) {
    return HospitalDTO.builder()
        .id(hospital.getId())
        .name(hospital.getName())
        .address(hospital.getAddress())
        .roadname(hospital.getRoadname())
        .zip(hospital.getZip())
        .tel(hospital.getTel())
        .lng(hospital.getLng())
        .lat(hospital.getLat())
        .build();
  }
}
