package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Room;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mapping(target = "groupName", source = "group.groupName") // Sửa groupId → groupName
  @Mapping(target = "equipments", source = "equipments")
  RoomDTO toRoomDTO(Room room);

  @Mapping(target = "group", source = "groupName", qualifiedByName = "mapGroupNameToGroup")
  @Mapping(target = "equipments", source = "equipments")
  Room toEntity(RoomDTO roomDTO);

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

  // Map từ groupName → GroupEntity
  @Named("mapGroupNameToGroup")
  default GroupEntity mapGroupNameToGroup(String groupName) {
    if (groupName == null) {
      return null;
    }
    GroupEntity group = new GroupEntity();
    group.setGroupName(groupName);
    return group;
  }

  // Map từ GroupEntity → groupName
  @Named("mapGroupToGroupName")
  default String mapGroupToGroupName(GroupEntity group) {
    return group != null ? group.getGroupName() : null;
  }

  List<EquipmentDTO> mapEquipments(List<Equipment> equipments);

  List<Equipment> mapEquipmentDTOs(List<EquipmentDTO> equipmentDTOs);
}

