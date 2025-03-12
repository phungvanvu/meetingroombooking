package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {

}
