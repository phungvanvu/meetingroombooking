package org.training.meetingroombooking.repository;

import java.util.List;
import javax.xml.stream.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Room;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomName(String roomName);
    List<Room> findByRoomNames(List<String> roomNames);
    List<Room> findByCapacity(List<Integer> capacity);
    List<Room> findByLocation(List<String> locations);
    List<Room> findByRoomNameAndLocation(List<String> roomNames, List<String> locations);
    List<Room> findByRoomNameAndCapacity(List<String> roomNames, List<Integer> capacity);
    List<Room> findByCapacityAndLocation(List<Integer> capacity, List<String> locations);
    List<Room> findByRoomNameAndCapacityAndLocation(List<String> roomNames, List<Integer> capacity, List<String> locations);
}
