package org.training.meetingroombooking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.training.meetingroombooking.entity.models.Room;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    Optional<Room> findByRoomName(String roomName);
    // Phòng có khả dụng
    long countByAvailable(boolean available);
}