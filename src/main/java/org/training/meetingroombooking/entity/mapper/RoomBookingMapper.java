package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.models.RoomBooking;

@Mapper(componentModel = "spring")
public interface RoomBookingMapper {
  RoomBooking toEntity(RoomBookingDTO dto);
  RoomBookingDTO toDTO(RoomBooking entity);
}
