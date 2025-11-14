package com.four.animory.service;

import com.four.animory.domain.region.Hospital;
import com.four.animory.dto.region.HospitalDTO;
import com.four.animory.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
  private final HospitalRepository hospitalRepository;

  @Override
  public List<HospitalDTO> findInBounds(double minLng, double maxLng, double minLat, double maxLat) {
    List<Hospital> hospital = hospitalRepository.findByLngBetweenAndLatBetween(minLng, maxLng, minLat, maxLat);
    List<HospitalDTO> hospitalDTOs = new ArrayList<>();
    for (Hospital h : hospital) {
      hospitalDTOs.add(entityToDTO(h));
    }
    return hospitalDTOs;
  }
}
