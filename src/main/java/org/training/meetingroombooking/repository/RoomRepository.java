package org.training.meetingroombooking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.training.meetingroombooking.entity.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
  List<Room> findAllByAvailableTrue();

  @Query("SELECT COUNT(r) FROM Room r")
  long countTotalRooms();

  @Query("SELECT COUNT(r) FROM Room r WHERE r.available = true")
  long countAvailableRooms();

  @Query("SELECT COUNT(r) FROM Room r WHERE r.available = false")
  long countUnavailableRooms();

  @Query("SELECT r FROM Room r WHERE r.available = true")
  List<Room> findAvailableRooms();

  @Query("SELECT r FROM Room r WHERE r.available = false")
  List<Room> findUnavailableRooms();
}
