package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.RoomBooking;

import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking,Long> {
    List<RoomBooking> findByBookedBy_UserName(String userName);
}
