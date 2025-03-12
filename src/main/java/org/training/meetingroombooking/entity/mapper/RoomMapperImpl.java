package org.training.meetingroombooking.entity.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.models.Room;

@Component
public class RoomMapperImpl implements RoomMapper {


  @Override
  public RoomDTO toDTO(Room entity) {
    if (entity == null) {
      return null;
    }
    return RoomDTO.builder()
        .roomName(entity.getRoomName())
        .location(entity.getLocation())
        .capacity(entity.getCapacity())
        .build();
  }

  @Override
  public Room toEntity(RoomDTO dto) {
    return null;
  }
}
