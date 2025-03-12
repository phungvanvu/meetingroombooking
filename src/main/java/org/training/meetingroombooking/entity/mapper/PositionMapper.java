package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.models.Position;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {

  PositionDTO toDTO(Position entity);

  Position toEntity(PositionDTO dto);
}

