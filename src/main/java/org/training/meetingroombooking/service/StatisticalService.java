package org.training.meetingroombooking.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticalService {
    private final RoomRepository roomRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final RoomBookingMapper roomBookingMapper;

    public StatisticalService(RoomBookingRepository roomBookingRepository,
                              RoomBookingMapper roomBookingMapper, RoomRepository roomRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.roomBookingMapper = roomBookingMapper;
        this.roomRepository = roomRepository;
    }

    public RoomSummaryDTO getMostBookedRoomOfMonth() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();

        Optional<Object[]> result = roomBookingRepository.findMostBookedRoomOfMonth(month, year);
        if (result.isPresent()) {
            Long roomId = (Long) result.get()[0];
            long count = (Long) result.get()[1];
            return new RoomSummaryDTO(roomId, "Most booked room of the month", count);
        }
        throw new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND);
    }

    public long getMonthlyBookingCount(int month, int year) {
        return roomBookingRepository.countBookingsByMonth(month, year);
    }

    public long getCurrentMonthBookingCount() {
        LocalDate currentDate = LocalDate.now();
        return getMonthlyBookingCount(currentDate.getMonthValue(), currentDate.getYear());
    }
    // Tổng lượt đặt phòng theo tuần
    public long getWeeklyBookingCount(int week, int year) {
        return roomBookingRepository.countBookingsByWeek(week, year);
    }

    // Tổng lượt đặt phòng theo quý
    public long getQuarterlyBookingCount(int quarter, int year) {
        return roomBookingRepository.countBookingsByQuarter(quarter, year);
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
        long totalRooms = roomRepository.count();  // Tổng số phòng
        long availableRooms = roomRepository.countByAvailable(true);  // Tổng số phòng có sẵn
        long unavailableRooms = roomRepository.countByAvailable(false);  // Tổng số phòng không có sẵn
        return new RoomStatisticsDTO(totalRooms, availableRooms, unavailableRooms);
    }
    public CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
