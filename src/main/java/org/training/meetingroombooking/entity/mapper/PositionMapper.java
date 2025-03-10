package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.models.Position;

public interface PositionMapper {
  PositionDTO toDTO(Position entity);
  Position toEntity(PositionDTO dto);
}
