package com.four.animory.repository;

import com.four.animory.domain.region.Hospital;
import com.four.animory.dto.region.HospitalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
  List<Hospital> findByLngBetweenAndLatBetween(
      double minLng, double maxLng,
      double minLat, double maxLat
  );
}
