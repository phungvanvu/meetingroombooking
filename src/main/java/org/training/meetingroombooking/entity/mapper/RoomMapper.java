package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapEquipmentListToStringList")
  RoomDTO toDTO(Room entity);

  @Mapping(target = "roomId", source = "roomId")
  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapStringListToEquipmentList")
  Room toEntity(RoomDTO dto);

  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapStringListToEquipmentList")
  void updateRoom(@MappingTarget Room entity, RoomDTO dto);

  // Chuyển từ List<Equipment> → List<String>
  @Named("mapEquipmentListToStringList")
  default List<String> mapEquipmentListToStringList(List<Equipment> equipments) {
    if (equipments == null) {
      return new ArrayList<>();
    }
    return equipments.stream()
            .map(Equipment::getName)
            .collect(Collectors.toList());
  }

  // Chuyển từ List<String> → List<Equipment>
  @Named("mapStringListToEquipmentList")
  default List<Equipment> mapStringListToEquipmentList(List<String> equipmentNames) {
    if (equipmentNames == null) {
      return new ArrayList<>();
    }
    return equipmentNames.stream()
            .map(name -> {
              Equipment equipment = new Equipment();
              equipment.setName(name);
              return equipment;
            })
            .collect(Collectors.toList());
  }

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
}

