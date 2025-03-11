package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
