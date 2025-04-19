package org.training.meetingroombooking.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.dto.Summary.*;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.mapper.RoomMapper;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.service.StatisticalService;

@Service
public class StatisticalServiceImpl implements StatisticalService {

  private final RoomRepository roomRepository;
  private final RoomBookingRepository roomBookingRepository;
  private final RoomMapper roomMapper;
  private final RoomBookingMapper roomBookingMapper;

  public StatisticalServiceImpl(
      RoomBookingRepository roomBookingRepository,
      RoomRepository roomRepository,
      RoomMapper roomMapper,
      RoomBookingMapper roomBookingMapper) {
    this.roomBookingRepository = roomBookingRepository;
    this.roomRepository = roomRepository;
    this.roomMapper = roomMapper;
    this.roomBookingMapper = roomBookingMapper;
  }

  @Override
  public List<UserSummaryDTO> getTopUsers(int limit) {
    return roomBookingRepository.findTopUsers(PageRequest.of(0, limit));
  }

  public RoomStatisticsDTO getRoomStatistics() {
    LocalDate today = LocalDate.now();
    long totalRooms = roomRepository.countTotalRooms();
    long availableRooms = roomRepository.countAvailableRooms();
    long unavailableRooms = roomRepository.countUnavailableRooms();
    long totalBookings = roomBookingRepository.countTotalBookings();
    long todayBookings = roomBookingRepository.countBookingsToday(today);
    List<RoomDTO> availableRoomList = roomRepository.findAvailableRooms()
            .stream().map(roomMapper::toDTO).toList();
    List<RoomDTO> unavailableRoomList = roomRepository.findUnavailableRooms()
            .stream().map(roomMapper::toDTO).toList();
    List<RoomBookingDTO> todayBookingList = roomBookingRepository.findBookingsToday(today)
            .stream().map(roomBookingMapper::toDTO).toList();
    return RoomStatisticsDTO.builder()
            .totalRooms(totalRooms)
            .availableRooms(availableRooms)
            .unavailableRooms(unavailableRooms)
            .totalBookings(totalBookings)
            .todayBookings(todayBookings)
            .availableRoomList(availableRoomList)
            .unavailableRoomList(unavailableRoomList)
            .todayBookingList(todayBookingList)
            .build();
  }
  @Override
  public List<BookingSummaryDTO> getWeeklyBookings() {
    return roomBookingRepository.findWeeklyBookingsNative()
            .stream()
            .map(p -> new BookingSummaryDTO(p.getPeriod(), p.getBookings()))
            .toList();
  }

  @Override
  public List<BookingSummaryDTO> getMonthlyBookings() {
    return roomBookingRepository.findMonthlyBookings();
  }

  @Override
  public List<BookingSummaryDTO> getQuarterlyBookings() {
    return roomBookingRepository.findQuarterlyBookings();
  }

  @Override
  public BookingSummaryDTO getCurrentMonthBookings() {
    int currentMonth = LocalDate.now().getMonthValue();
    long count = roomBookingRepository.countByMonth(currentMonth);
    return new BookingSummaryDTO(currentMonth, count);
  }

  @Override
  public RoomSummaryDTO getMostBookedRoom() {
    List<RoomSummaryDTO> topRooms =
            roomBookingRepository.findTopBookedRooms(PageRequest.of(0, 1));
    return topRooms.isEmpty() ? null : topRooms.get(0);
  }
}
