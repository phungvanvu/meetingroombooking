package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.mapper.RoomMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticalService {
    private final RoomRepository roomRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final RoomMapper roomMapper;
    private final RoomBookingMapper roomBookingMapper;

    public StatisticalService(RoomBookingRepository roomBookingRepository, RoomRepository roomRepository,
                              RoomMapper roomMapper, RoomBookingMapper roomBookingMapper) {
        this.roomBookingRepository = roomBookingRepository;
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomBookingMapper = roomBookingMapper;
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
        List<Room> allRooms = roomRepository.findAll();
        long totalRooms = allRooms.size();
        List<Room> availableRooms = allRooms.stream()
                .filter(Room::isAvailable)
                .toList();
        List<Room> unavailableRooms = allRooms.stream()
                .filter(r -> !r.isAvailable())
                .toList();
        List<RoomBooking> allBookings = roomBookingRepository.findAll();
        long totalBookings = allBookings.size();
        LocalDate today = LocalDate.now();
        List<RoomBooking> todayBookings = allBookings.stream()
                .filter(b -> b.getStartTime().toLocalDate().equals(today))
                .toList();
        List<RoomDTO> availableRoomDTOs = availableRooms.stream()
                .map(roomMapper::toDTO)
                .toList();
        List<RoomDTO> unavailableRoomDTOs = unavailableRooms.stream()
                .map(roomMapper::toDTO)
                .toList();
        List<RoomBookingDTO> todayBookingDTOs = todayBookings.stream()
                .map(roomBookingMapper::toDTO)
                .toList();
        return RoomStatisticsDTO.builder()
                .totalRooms(totalRooms)
                .availableRooms(availableRooms.size())
                .unavailableRooms(unavailableRooms.size())
                .totalBookings(totalBookings)
                .todayBookings(todayBookings.size())
                .availableRoomList(availableRoomDTOs)
                .unavailableRoomList(unavailableRoomDTOs)
                .todayBookingList(todayBookingDTOs)
                .build();
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
