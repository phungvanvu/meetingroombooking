package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapEquipmentSetToStringSet")
  RoomDTO toDTO(Room entity);

  @Mapping(target = "roomId", source = "roomId")
  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapStringSetToEquipmentSet")
  Room toEntity(RoomDTO dto);

  @Mapping(target = "equipments", source = "equipments", qualifiedByName = "mapStringSetToEquipmentSet")
  void updateRoom(@MappingTarget Room entity, RoomDTO dto);

  // Chuyển từ Set<Equipment> → Set<String> (lấy equipmentName)
  @Named("mapEquipmentSetToStringSet")
  default Set<String> mapEquipmentSetToStringSet(Set<Equipment> equipments) {
    if (equipments == null) {
      return new HashSet<>();
    }
    return equipments.stream()
            .map(Equipment::getEquipmentName)
            .collect(Collectors.toSet());
  }

  // Chuyển từ Set<String> → Set<Equipment>
  @Named("mapStringSetToEquipmentSet")
  default Set<Equipment> mapStringSetToEquipmentSet(Set<String> equipmentNames) {
    if (equipmentNames == null) {
      return new HashSet<>();
    }
    return equipmentNames.stream()
            .map(name -> {
              Equipment equipment = new Equipment();
              equipment.setEquipmentName(name);
              return equipment;
            })
            .collect(Collectors.toSet());
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

