package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.models.Equipment;

public interface EquipmentMapper {
  Equipment toEntity(EquipmentDTO dto);
  EquipmentDTO toDTO(Equipment entity);
  void updateEntity(Equipment entity, EquipmentDTO dto);

}
