package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.mapstruct.*;
import org.training.meetingroombooking.entity.models.Room;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
  EquipmentDTO toDTO(Equipment entity);
  Equipment toEntity(EquipmentDTO dto);
  void updateEntity(@MappingTarget Equipment entity, EquipmentDTO dto);
}
