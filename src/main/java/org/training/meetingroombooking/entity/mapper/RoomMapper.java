package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Room;

public interface RoomMapper {
  RoomDTO toDTO(Room entity);
  Room toEntity(RoomDTO dto);
}
