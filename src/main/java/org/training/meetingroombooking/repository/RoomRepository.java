package org.training.meetingroombooking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Room;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomName(String roomName);
    List<Room> findByRoomNameIn(List<String> roomNames);
    List<Room> findByCapacity(List<Integer> capacity);
    List<Room> findByLocationIn(List<String> locations);
    List<Room> findByRoomNameInAndLocationIn(List<String> roomNames, List<String> locations);
    List<Room> findByRoomNameInAndCapacity(List<String> roomNames, List<Integer> capacity);
    List<Room> findByCapacityAndLocationIn(List<Integer> capacity, List<String> locations);
    List<Room> findByRoomNameInAndCapacityAndLocationIn(List<String> roomNames, List<Integer> capacity, List<String> locations);
}