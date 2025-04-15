package org.training.meetingroombooking.repository;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findAllByAvailableTrue();
    boolean existsByEquipments(Set<Equipment> equipments);
}