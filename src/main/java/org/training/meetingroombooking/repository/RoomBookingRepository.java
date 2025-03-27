package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.training.meetingroombooking.entity.models.RoomBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomBookingRepository extends JpaRepository<RoomBooking,Long> {
    List<RoomBooking> findByBookedBy_UserName(String userName);

    @Query("SELECT COUNT(rb) FROM RoomBooking rb WHERE MONTH(rb.startTime) = :month AND YEAR(rb.startTime) = :year")
    long countBookingsByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(rb) FROM RoomBooking rb WHERE WEEK(rb.startTime) = :week AND YEAR(rb.startTime) = :year")
    long countBookingsByWeek(int week, int year);

    @Query("SELECT COUNT(rb) FROM RoomBooking rb WHERE QUARTER(rb.startTime) = :quarter AND YEAR(rb.startTime) = :year")
    long countBookingsByQuarter(int quarter, int year);

    @Query("SELECT r FROM RoomBooking r WHERE r.startTime BETWEEN :start AND :end")
    List<RoomBooking> findMeetingsBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT rb.room.roomId, COUNT(rb) FROM RoomBooking rb " +
            "WHERE MONTH(rb.startTime) = :month AND YEAR(rb.startTime) = :year " +
            "GROUP BY rb.room.roomId " +
            "ORDER BY COUNT(rb) DESC LIMIT 1")
    Optional<Object[]> findMostBookedRoomOfMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT rb.bookedBy.userId, rb.bookedBy.userName, COUNT(rb) AS booking_count " +
            "FROM RoomBooking rb " +
            "GROUP BY rb.bookedBy.userId, rb.bookedBy.userName " +
            "ORDER BY booking_count DESC")
    List<Object[]> findTopUsers(int limit);

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

}
