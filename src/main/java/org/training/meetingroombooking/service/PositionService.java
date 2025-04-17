package org.training.meetingroombooking.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.training.meetingroombooking.entity.dto.PositionDTO;

public interface PositionService {
  PositionDTO create(PositionDTO dto);

  List<PositionDTO> getAll();

  Page<PositionDTO> getPositions(
      String positionName,
      String description,
      int page,
      int size,
      String sortBy,
      String sortDirection);

  PositionDTO update(String positionName, PositionDTO dto);

  void deletePosition(String positionName);

  void deleteMultiplePositions(List<String> positionName);
}
