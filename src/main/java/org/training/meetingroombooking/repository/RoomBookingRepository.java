package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.RoomBooking;

public interface RoomBookingRepository extends JpaRepository<RoomBooking,Long> {

}
