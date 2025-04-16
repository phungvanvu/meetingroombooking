package org.training.meetingroombooking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.training.meetingroombooking.entity.models.Equipment;

public interface EquipmentRepository
    extends JpaRepository<Equipment, String>, JpaSpecificationExecutor<Equipment> {
  Optional<Equipment> findByEquipmentName(String equipmentName);
}
