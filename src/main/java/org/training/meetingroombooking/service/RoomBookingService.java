package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;

public interface RoomBookingService {

  RoomBookingDTO create(RoomBookingDTO dto);

  Page<RoomBookingDTO> searchMyBookings(
      String roomName,
      LocalDateTime fromTime,
      LocalDateTime toTime,
      BookingStatus status,
      int page,
      int size);

  Page<RoomBookingDTO> searchBookings(
      String roomName,
      LocalDateTime fromTime,
      LocalDateTime toTime,
      BookingStatus status,
      String bookedByName,
      int page,
      int size);

  List<RoomBookingDTO> getAll();

  List<RoomBookingDTO> getBookingsByUserName(String userName);

  RoomBookingDTO update(Long bookingId, RoomBookingDTO dto);

  void delete(Long bookingId);

  ByteArrayOutputStream exportBookingsToExcel() throws IOException;

  void sendMeetingReminderEmails();

  List<RoomBookingDTO> getBookingsByRoomId(Long roomId);

  List<RoomBookingDTO> getUpcomingBookings();

  List<RoomBookingDTO> getUpcomingBookingsByUserName(String userName);

  List<RoomBookingDTO> getUpcomingBookingsByRoomId(Long roomId);

  Page<RoomBookingDTO> getMyUpcomingBookings(
      String roomName, LocalDateTime fromTime, LocalDateTime toTime, int page, int size);

  RoomBookingDTO cancelBooking(Long bookingId);

  void cancelMultipleBookings(List<Long> bookingIds);
}
