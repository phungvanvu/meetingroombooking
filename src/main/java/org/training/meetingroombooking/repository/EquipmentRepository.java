package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Equipment;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    // Lấy danh sách thiết bị không khả dụng
    List<Equipment> findByAvailableFalse();

    // Đếm số thiết bị khả dụng
    long countByAvailableTrue();

    // Đếm số thiết bị không khả dụng
    long countByAvailableFalse();
}
