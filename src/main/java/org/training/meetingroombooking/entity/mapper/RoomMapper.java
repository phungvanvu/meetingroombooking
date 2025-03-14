package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Room;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mapping(target = "equipments", source = "equipments")
  RoomDTO toDTO(Room entity);

  @Mapping(target = "equipments", source = "equipments")
  Room toEntity(RoomDTO dto);

  @Mapping(target = "equipments", source = "equipments")
  void updateRoom(@MappingTarget Room entity, RoomDTO dto);

  // Map từ Equipment → EquipmentDTO
  @Mapping(target = "room", source = "room", qualifiedByName = "mapRoomToRoomId")
  EquipmentDTO toEquipmentDTO(Equipment equipment);

  // Map từ EquipmentDTO → Equipment
  @Mapping(target = "room", source = "room", qualifiedByName = "mapRoomIdToRoom")
  Equipment toEquipment(EquipmentDTO equipmentDTO);

  // Map từ Room → roomId
  @Named("mapRoomToRoomId")
  default Long mapRoomToRoomId(Room room) {
    return room != null ? room.getRoomId() : null;
  }

  @Named("mapRoomIdToRoom")
  default Room mapRoomIdToRoom(Long roomId) {
    if (roomId == null) {
      return null;
    }
    Room room = new Room();
    room.setRoomId(roomId);
    return room;
  }

  List<EquipmentDTO> mapEquipments(List<Equipment> equipments);

  List<Equipment> mapEquipmentDTOs(List<EquipmentDTO> equipmentDTOs);
}

