package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.models.RoomBooking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking,Long> {
    List<RoomBooking> findByStatusAndStartTimeAfter(BookingStatus status, LocalDateTime time);
    List<RoomBooking> findByBookedBy_UserNameAndStatusAndStartTimeAfter(String userName, BookingStatus status, LocalDateTime time);
    List<RoomBooking> findByRoom_roomIdAndStatusAndStartTimeAfter(Long roomId, BookingStatus status, LocalDateTime time);

    List<RoomBooking> findByBookedBy_UserName(String userName);

    @Query("SELECT r FROM RoomBooking r WHERE r.startTime BETWEEN :start AND :end")
    List<RoomBooking> findMeetingsBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT rb.bookedBy.userId, rb.bookedBy.userName, COUNT(rb) AS booking_count " +
            "FROM RoomBooking rb " +
            "GROUP BY rb.bookedBy.userId, rb.bookedBy.userName " +
            "ORDER BY booking_count DESC")
    List<Object[]> findTopUsers(int limit);

    // Đếm số lượng đặt phòng hôm nay
    @Query("SELECT COUNT(b) FROM RoomBooking b WHERE DATE(b.startTime) = :bookingDate")
    long countByBookingDate(@Param("bookingDate") LocalDate bookingDate);

    //kiểm tra xem có bản ghi đặt phòng nào đã tồn tại cho cùng khoảng thời gian/phòng không
    @Query("""
        SELECT COUNT(rb) > 0 FROM RoomBooking rb
        WHERE rb.room.roomId = :roomId
        AND (
            (rb.startTime < :endTime AND rb.endTime > :startTime)
        )
        """)
    boolean existsByRoomAndTimeOverlap(@Param("roomId") Long roomId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    // Đếm số lượt đặt theo tuần
    @Query(value = "SELECT WEEK(created_at) as period, COUNT(*) as bookings " +
            "FROM room_bookings " +
            "GROUP BY WEEK(created_at)", nativeQuery = true)
    List<Object[]> findWeeklyBookings();

    @Query(value = "SELECT MONTH(created_at) as period, COUNT(*) as bookings " +
            "FROM room_bookings " +
            "GROUP BY MONTH(created_at)", nativeQuery = true)
    List<Object[]> findMonthlyBookings();

    // Đếm số lượt đặt theo quý
    @Query(value = "SELECT QUARTER(created_at) as period, COUNT(*) as bookings " +
            "FROM room_bookings " +
            "GROUP BY QUARTER(created_at)", nativeQuery = true)
    List<Object[]> findQuarterlyBookings();

    // Đếm số lượt đặt trong tháng hiện tại
    @Query(value = "SELECT COUNT(*) FROM room_bookings WHERE MONTH(created_at) = :month", nativeQuery = true)
    long countByMonth(@Param("month") int month);

    @Query(value = "SELECT rb.room_id, r.room_name, COUNT(rb.booking_id) AS booking_count " +
            "FROM room_bookings rb " +
            "JOIN rooms r ON rb.room_id = r.room_id " +
            "GROUP BY rb.room_id, r.room_name " +
            "ORDER BY booking_count DESC " +
            "LIMIT 1", nativeQuery = true)
    Object[] findMostBookedRoom();

}
