package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.PositionDTO;
import org.training.meetingroombooking.entity.Position;

public interface PositionMapper {
  PositionDTO toDTO(Position entity);
  Position toEntity(PositionDTO dto);
}
