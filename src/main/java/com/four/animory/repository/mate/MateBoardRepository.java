package com.four.animory.repository.mate;

import com.four.animory.domain.mate.MateBoard;
import com.four.animory.repository.sitter.SitterSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateBoardRepository extends JpaRepository<MateBoard, Long>, MateSearch{


}
