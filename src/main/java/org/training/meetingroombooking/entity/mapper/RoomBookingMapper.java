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

  @Mapping(target = "bookingId", source = "bookingId")
  @Mapping(target = "bookedById", source = "bookedBy.userId")
  @Mapping(target = "userName", source = "bookedBy.userName")
  @Mapping(target = "roomId", source = "room.roomId")
  @Mapping(target = "roomName", source = "room.roomName")
  @Mapping(target = "userEmail", source = "bookedBy.email")
  RoomBookingDTO toDTO(RoomBooking entity);

  @Mapping(target = "bookedBy", source = "bookedById", qualifiedByName = "mapUserIdToUser")
  @Mapping(target = "room", source = "roomId",qualifiedByName = "mapRoomIdToRoom")
  @Mapping(target = "status", ignore = true)
  void updateEntity(@MappingTarget RoomBooking entity, RoomBookingDTO dto);
}
