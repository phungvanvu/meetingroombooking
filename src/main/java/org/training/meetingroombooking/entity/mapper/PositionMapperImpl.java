package org.training.meetingroombooking.entity.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.models.Position;

@Component
public class PositionMapperImpl implements PositionMapper{
  @Override
  public Position toEntity(PositionDTO dto) {
    if (dto == null) {
      return null;
    }
    Position entity = new Position();
    entity.setPositionName(dto.getPositionName());
    entity.setDescription(dto.getDescription());
    return entity;
  }
  @Override
  public PositionDTO toDTO(Position entity) {
    if (entity == null) {
      return null;
    }
    return new PositionDTO(
        entity.getPositionName(),
        entity.getPositionName()
    );
  }
}
