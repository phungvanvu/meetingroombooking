package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticalService {
    private final RoomRepository roomRepository;
    private final RoomBookingRepository roomBookingRepository;

    public StatisticalService(RoomBookingRepository roomBookingRepository,
                              RoomRepository roomRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.roomRepository = roomRepository;
    }

    // Top người dùng đặt phòng nhiều nhất
    public List<UserSummaryDTO> getTopUsers(int limit) {
        return roomBookingRepository.findTopUsers(limit).stream()
                .map(result -> new UserSummaryDTO(
                        (Long) result[0], // userId
                        (String) result[1], // username
                        (Long) result[2]) // bookingCount
                )
                .toList();
    }

    public RoomStatisticsDTO getRoomStatistics() {
        long totalRooms = roomRepository.count(); // Tổng số phòng
        long availableRooms = roomRepository.countByAvailable(true); // Phòng có sẵn
        long unavailableRooms = roomRepository.countByAvailable(false); // Phòng không có sẵn
        long totalBookings = roomBookingRepository.count(); // Đếm tổng số lượng đặt phòng
        long todayBookings = roomBookingRepository
                .countByBookingDate(LocalDate.now()); // Đếm số lượng đặt phòng hôm nay
        return new RoomStatisticsDTO(
                totalRooms,
                availableRooms,
                unavailableRooms,
                totalBookings,
                todayBookings
        );
    }

    public List<BookingSummaryDTO> getWeeklyBookings() {
        List<Object[]> results = roomBookingRepository.findWeeklyBookings();
        return results.stream()
                .map(arr -> new BookingSummaryDTO(
                        ((Number) arr[0]).intValue(),
                        ((Number) arr[1]).intValue()
                ))
                .collect(Collectors.toList());
    }

    public List<BookingSummaryDTO> getMonthlyBookings() {
        List<Object[]> results = roomBookingRepository.findMonthlyBookings();
        return results.stream()
                .map(arr -> new BookingSummaryDTO(
                        ((Number) arr[0]).intValue(),
                        ((Number) arr[1]).intValue()
                ))
                .collect(Collectors.toList());
    }

    public List<BookingSummaryDTO> getQuarterlyBookings() {
        List<Object[]> results = roomBookingRepository.findQuarterlyBookings();
        return results.stream()
                .map(arr -> new BookingSummaryDTO(
                        ((Number) arr[0]).intValue(),
                        ((Number) arr[1]).intValue()
                ))
                .collect(Collectors.toList());
    }

    public BookingSummaryDTO getCurrentMonthBookings() {
        int currentMonth = LocalDate.now().getMonthValue();
        long count = roomBookingRepository.countByMonth(currentMonth);
        return new BookingSummaryDTO(currentMonth, (int) count);
    }

    public RoomSummaryDTO getMostBookedRoom() {
        Object[] result = roomBookingRepository.findMostBookedRoom();
        if (result != null && result.length == 1 && result[0] instanceof Object[]) {
            result = (Object[]) result[0];
        }
        if (result != null && result.length >= 3) {
            Long roomId = ((Number) result[0]).longValue();
            String roomName = (String) result[1];
            long bookingCount = ((Number) result[2]).longValue();
            return new RoomSummaryDTO(roomId, roomName, bookingCount);
        }
        return null;
    }
}
