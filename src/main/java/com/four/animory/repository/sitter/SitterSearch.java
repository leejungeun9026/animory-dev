package com.four.animory.repository.sitter;

import com.four.animory.domain.sitter.SitterBoard;
import com.four.animory.dto.sitter.SitterBoardListDTO;
import com.four.animory.dto.sitter.SitterBoardPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SitterSearch {
  Page<SitterBoardListDTO> search(String sido, String sigungu, String category, String state, String petInfo, String[] field, String keyword, Pageable pageable);
}
