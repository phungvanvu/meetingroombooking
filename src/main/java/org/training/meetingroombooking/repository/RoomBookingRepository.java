package org.training.meetingroombooking.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.training.meetingroombooking.entity.dto.Summary.*;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.entity.models.User;

public interface RoomBookingRepository
    extends JpaRepository<RoomBooking, Long>, JpaSpecificationExecutor<RoomBooking> {

  // Lấy booking theo trạng thái và thời gian bắt đầu
  List<RoomBooking> findByStatusAndStartTimeAfter(BookingStatus status, LocalDateTime time);

  // Lấy booking theo userName, trạng thái và thời gian bắt đầu
  List<RoomBooking> findByBookedBy_UserNameAndStatusAndStartTimeAfter(
      String userName, BookingStatus status, LocalDateTime time);

  // Lấy booking theo phòng, trạng thái và thời gian bắt đầu
  List<RoomBooking> findByRoom_roomIdAndStatusAndStartTimeAfter(
      Long roomId, BookingStatus status, LocalDateTime time);

  // Lấy booking đặt theo userName
  List<RoomBooking> findByBookedBy_UserName(String userName);

  // Đếm tổng số lượt đặt phòng
  @Query("SELECT COUNT(rb) FROM RoomBooking rb")
  long countTotalBookings();

  // Đếm số cuộc họp diễn ra trong hôm nay
  @Query("SELECT COUNT(rb) FROM RoomBooking rb WHERE DATE(rb.startTime) = :today")
  long countBookingsToday(@Param("today") LocalDate today);

  // Lấy danh sách cuộc họp diễn ra trong hôm nay
  @Query("SELECT rb FROM RoomBooking rb WHERE DATE(rb.startTime) = :today")
  List<RoomBooking> findBookingsToday(@Param("today") LocalDate today);

  // Lấy các booking diễn ra trong một khoảng thời gian cụ thể
  @Query("SELECT r FROM RoomBooking r WHERE r.startTime BETWEEN :start AND :end")
  List<RoomBooking> findMeetingsBetween(LocalDateTime start, LocalDateTime end);

  // Thống kê top users
  @Query(
      """
    SELECT new org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO(
      rb.bookedBy.userId, rb.bookedBy.userName, COUNT(rb))
    FROM RoomBooking rb
    GROUP BY rb.bookedBy.userId, rb.bookedBy.userName
    ORDER BY COUNT(rb) DESC
  """)
  List<UserSummaryDTO> findTopUsers(Pageable pageable);

  // Kiểm tra xem đã có phòng nào được đặt trong khoảng thời gian đó chưa (tránh trùng lịch)
  @Query(
      """
        SELECT COUNT(rb) > 0 FROM RoomBooking rb
        WHERE rb.room.roomId = :roomId
        AND rb.status = 'CONFIRMED'
        AND (rb.startTime < :endTime AND rb.endTime > :startTime)
        """)
  boolean existsByRoomAndTimeOverlap(
      @Param("roomId") Long roomId,
      @Param("startTime") LocalDateTime startTime,
      @Param("endTime") LocalDateTime endTime);

  // Kiểm tra trùng lịch nhưng loại trừ chính bản ghi hiện tại (dùng khi cập nhật)
  @Query(
      """
        SELECT COUNT(rb) > 0 FROM RoomBooking rb
        WHERE rb.room.roomId = :roomId
        AND rb.status = 'CONFIRMED'
        AND rb.startTime < :endTime AND rb.endTime > :startTime
        AND rb.bookingId <> :bookingId
        """)
  boolean existsByRoomAndTimeOverlapExcludingId(
      @Param("roomId") Long roomId,
      @Param("startTime") LocalDateTime startTime,
      @Param("endTime") LocalDateTime endTime,
      @Param("bookingId") Long bookingId);

  /**
   * Weekly booking summary for a given year
   */
  @Query(value = """
    SELECT WEEK(rb.created_at, 1)     AS period,
           COUNT(*)                   AS bookings
      FROM room_bookings rb
     WHERE YEAR(rb.created_at) = :year
  GROUP BY WEEK(rb.created_at, 1)
  ORDER BY WEEK(rb.created_at, 1)
  """, nativeQuery = true)
  List<BookingSummaryDTO> findWeeklyBookings(@Param("year") int year);

  /**
   * Monthly booking summary for a given year
   */
  @Query("""
    SELECT new org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO(
      MONTH(rb.startTime),
      COUNT(rb)
    )
    FROM RoomBooking rb
    WHERE YEAR(rb.startTime) = :year
    GROUP BY MONTH(rb.startTime)
    ORDER BY MONTH(rb.startTime)
  """ )
  List<BookingSummaryDTO> findMonthlyBookings(@Param("year") int year);

  /**
   * Quarterly booking summary for a given year
   */
  @Query(
          """
            SELECT new org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO(
              FLOOR((MONTH(rb.startTime) - 1) / 3) + 1,
              COUNT(rb)
            )
            FROM RoomBooking rb
            WHERE YEAR(rb.startTime) = :year
            GROUP BY FLOOR((MONTH(rb.startTime) - 1) / 3) + 1
            ORDER BY 1
          """
  )
  List<BookingSummaryDTO> findQuarterlyBookings(@Param("year") int year);


  // Đếm số lượt đặt trong tháng (theo tháng truyền vào)
  @Query(
      value = "SELECT COUNT(*) FROM room_bookings WHERE MONTH(created_at) = :month",
      nativeQuery = true)
  long countByMonth(@Param("month") int month);

  // Top booked rooms
  @Query(
      """
      SELECT new org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO(
        rb.room.roomId, rb.room.roomName, COUNT(rb)
      )
      FROM RoomBooking rb
      GROUP BY rb.room.roomId, rb.room.roomName
      ORDER BY COUNT(rb) DESC
    """)
  List<RoomSummaryDTO> findTopBookedRooms(Pageable pageable);

  // Kiểm tra xem người dùng có từng đặt phòng chưa
  boolean existsByBookedBy(User user);

  // Kiểm tra xem phòng này đã từng có ai đặt chưa
  boolean existsByRoom(Room room);
}
