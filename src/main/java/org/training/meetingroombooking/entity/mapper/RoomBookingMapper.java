package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.models.RoomBooking;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoomMapper.class})
public interface RoomBookingMapper {
  @Mapping(target = "bookedBy", source = "bookedById", qualifiedByName = "mapUserIdToUser")
  @Mapping(target = "room", source = "roomId",qualifiedByName = "mapRoomIdToRoom")
  RoomBooking toEntity(RoomBookingDTO dto);

  @Mapping(target = "bookedById", source = "bookedBy.userId")
  @Mapping(target = "roomId", source = "room.roomId")
  RoomBookingDTO toDTO(RoomBooking entity);

  @Mapping(target = "bookedBy", source = "bookedById", qualifiedByName = "mapUserIdToUser")
  @Mapping(target = "room", source = "roomId",qualifiedByName = "mapRoomIdToRoom")
  void updateEntity(@MappingTarget RoomBooking entity, RoomBookingDTO dto);
}
