package org.training.meetingroombooking.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.repository.RoomRepository;

@Component
public class EquipmentMapperImpl implements EquipmentMapper {

  @Autowired
  private RoomRepository roomRepository;

  @Override
  public Equipment toEntity(EquipmentDTO dto){
    if (dto == null){
      return null;
    }
    Equipment equipment = new Equipment();
    equipment.setName(dto.getName());
    equipment.setDescription(dto.getDescription());
    equipment.setQuantity(dto.getQuantity());
    equipment.setAvailable(dto.getAvailable());
    if (dto.getRoom() != null){
      equipment.setRoom(roomRepository.findById(dto.getRoom()).orElse(null));
    }
    return equipment;
  }

  @Override
  public EquipmentDTO toDTO(Equipment entity) {
    if (entity == null){
      return null;
    }
    return EquipmentDTO.builder()
        .name(entity.getName())
        .description(entity.getDescription())
        .quantity(entity.getQuantity())
        .available(entity.isAvailable())
        .room(entity.getRoom() != null ? entity.getRoom().getRoomId() : null)
        .build();
  }

  @Override
  public void updateEntity(Equipment entity, EquipmentDTO dto) {

  }


}
