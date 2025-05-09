package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.models.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

  PositionDTO toDTO(Position entity);

  Position toEntity(PositionDTO dto);

  void updatePosition(@MappingTarget Position entity, PositionDTO dto);
}
