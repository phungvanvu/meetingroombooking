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

    @Query("SELECT r FROM RoomBooking r WHERE r.startTime BETWEEN :start AND :end")
    List<RoomBooking> findMeetingsBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT rb.room.roomId, COUNT(rb) FROM RoomBooking rb " +
            "WHERE MONTH(rb.startTime) = :month AND YEAR(rb.startTime) = :year " +
            "GROUP BY rb.room.roomId " +
            "ORDER BY COUNT(rb) DESC LIMIT 1")
    Optional<Object[]> findMostBookedRoomOfMonth(@Param("month") int month, @Param("year") int year);

}
