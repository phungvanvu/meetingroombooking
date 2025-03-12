package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.mapstruct.*;
import org.training.meetingroombooking.entity.models.Room;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

  @Mapping(target = "room", source = "room.roomId")
  EquipmentDTO toDTO(Equipment entity);

  @Mapping(target = "room", source = "room", qualifiedByName = "mapRoomIdToRoom")
  Equipment toEntity(EquipmentDTO dto);

  @Named("mapRoomIdToRoom")
  default Room mapRoomIdToRoom(Long roomId) {
    if (roomId == null) {
      return null;
    }
    return Room.builder().roomId(roomId).build();
  }

  @Mapping(target = "room", source = "room", qualifiedByName = "mapRoomIdToRoom")
  void updateEntity(@MappingTarget Equipment entity, EquipmentDTO dto);
}
