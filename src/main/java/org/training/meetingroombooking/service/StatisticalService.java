package org.training.meetingroombooking.service;

import java.util.List;
import org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;

public interface StatisticalService {
  List<UserSummaryDTO> getTopUsers(int limit);

  RoomStatisticsDTO getRoomStatistics();

  List<BookingSummaryDTO> getWeeklyBookings();

  List<BookingSummaryDTO> getMonthlyBookings();

  List<BookingSummaryDTO> getQuarterlyBookings();

  BookingSummaryDTO getCurrentMonthBookings();

  RoomSummaryDTO getMostBookedRoom();
}
