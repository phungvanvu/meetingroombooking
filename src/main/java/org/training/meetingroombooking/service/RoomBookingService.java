package org.training.meetingroombooking.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;

@Service
public class RoomBookingService {

  private final RoomBookingRepository roomBookingRepository;
  private final RoomBookingMapper roomBookingMapper;

  public RoomBookingService(RoomBookingRepository roomBookingRepository, RoomBookingMapper roomBookingMapper) {
    this.roomBookingRepository = roomBookingRepository;
    this.roomBookingMapper = roomBookingMapper;
  }

  public RoomBookingDTO create(RoomBookingDTO dto) {
    RoomBooking roomBooking = roomBookingMapper.toEntity(dto);
    RoomBooking savedRoomBooking = roomBookingRepository.save(roomBooking);
    return roomBookingMapper.toDTO(savedRoomBooking);
  }
  public List<RoomBookingDTO> getAll() {
    List<RoomBooking> roomBookings = roomBookingRepository.findAll();
    return roomBookings.stream()
        .map(roomBookingMapper::toDTO)
        .collect(Collectors.toList());
  }

  public RoomBookingDTO update(Long bookingId, RoomBookingDTO dto) {
    Optional<RoomBooking> existingRoomBooking = roomBookingRepository.findById(bookingId);
    if (existingRoomBooking.isPresent()) {
      RoomBooking roomBooking = existingRoomBooking.get();
      roomBookingMapper.updateEntity(roomBooking, dto);
      RoomBooking updatedRoomBooking = roomBookingRepository.save(roomBooking);
      return roomBookingMapper.toDTO(updatedRoomBooking);
    }
    throw new AppEx(ErrorCode.ROOMBOOKING_NOT_FOUND);
  }
  public void delete(Long bookingId) {
    if (roomBookingRepository.existsById(bookingId)) {
      throw new AppEx(ErrorCode.ROOMBOOKING_NOT_FOUND);
    }
    roomBookingRepository.deleteById(bookingId);
  }
}
