package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.RoomEquipment;

public interface RoomEquipmentRepository extends JpaRepository<RoomEquipment, Long> {
    boolean existsByEquipment_EquipmentName(String equipmentName);
}
