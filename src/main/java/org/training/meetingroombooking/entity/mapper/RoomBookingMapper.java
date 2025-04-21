package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.models.RoomBooking;

@Mapper(componentModel = "spring")
public interface RoomBookingMapper {
  RoomBooking toEntity(RoomBookingDTO dto);

  @Mapping(target = "bookedById", source = "bookedBy.userId")
  @Mapping(target = "userName", source = "bookedBy.userName")
  @Mapping(target = "roomId", source = "room.roomId")
  @Mapping(target = "roomName", source = "room.roomName")
  @Mapping(target = "userEmail", source = "bookedBy.email")
  RoomBookingDTO toDTO(RoomBooking entity);

  void updateEntity(@MappingTarget RoomBooking entity, RoomBookingDTO dto);
}
