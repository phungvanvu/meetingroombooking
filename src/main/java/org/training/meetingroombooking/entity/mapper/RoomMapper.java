package org.training.meetingroombooking.entity.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomEquipment;
import org.training.meetingroombooking.entity.models.RoomImage;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mapping(
          target = "equipments",
          source = "roomEquipments",
          qualifiedByName = "mapRoomEquipmentToStringSet")
  @Mapping(
          target = "imageUrls",
          source = "images",
          qualifiedByName = "mapRoomImageToStringList")
  RoomDTO toDTO(Room entity);

  @Mapping(
      target = "roomEquipments",
      source = "equipments",
      qualifiedByName = "mapStringSetToRoomEquipmentSet")
  Room toEntity(RoomDTO dto);

  @Mapping(
          target = "roomEquipments",
          source = "equipments",
          qualifiedByName = "mapStringSetToRoomEquipmentSet")
  void updateRoom(@MappingTarget Room entity, RoomDTO dto);

  @Named("mapRoomEquipmentToStringSet")
  default Set<String> mapRoomEquipmentToStringSet(Set<RoomEquipment> roomEquipments) {
    if (roomEquipments == null) {
      return new HashSet<>();
    }
    return roomEquipments.stream()
        .map(roomEquipment -> roomEquipment.getEquipment().getEquipmentName())
        .collect(Collectors.toSet());
  }

  @Named("mapStringSetToRoomEquipmentSet")
  default Set<RoomEquipment> mapStringSetToRoomEquipmentSet(Set<String> equipmentNames) {
    if (equipmentNames == null) {
      return new HashSet<>();
    }
    return equipmentNames.stream()
        .map(
            name -> {
              RoomEquipment roomEquipment = new RoomEquipment();
              Equipment equipment = new Equipment();
              equipment.setEquipmentName(name);
              roomEquipment.setEquipment(equipment);
              return roomEquipment;
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

  @Named("mapRoomImageToStringList")
  default List<String> mapRoomImageToStringList(List<RoomImage> images) {
    if (images == null) return List.of();
    return images.stream().map(RoomImage::getUrl).collect(Collectors.toList());
  }
}
