package org.training.meetingroombooking.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.EquipmentMapper;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.EquipmentRepository;

@Service
public class EquipmentService {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public EquipmentService(EquipmentRepository equipmentRepository,
      EquipmentMapper equipmentMapper) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
  }

  public EquipmentDTO create(EquipmentDTO equipmentDTO) {
    Equipment entity = equipmentMapper.toEntity(equipmentDTO);
    Equipment savedEquipment = equipmentRepository.save(entity);
    return equipmentMapper.toDTO(savedEquipment);
  }

  public List<EquipmentDTO> getAll() {
    return equipmentRepository.findAll()
        .stream()
        .map(equipmentMapper::toDTO)
        .toList();
  }

  public EquipmentDTO getById(long id) {
    Optional<Equipment> equipment = equipmentRepository.findById(id);
    return equipment.map(equipmentMapper::toDTO).orElseThrow(
        () -> new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND));
  }

  public EquipmentDTO update(Long id, EquipmentDTO equipmentDTO) {
    Optional<Equipment> existingEquipment = equipmentRepository.findById(id);
    if (existingEquipment.isPresent()) {
      Equipment equipment = existingEquipment.get();
      equipmentMapper.updateEntity(equipment, equipmentDTO);
      Equipment updatedEquipment = equipmentRepository.save(equipment);
      return equipmentMapper.toDTO(updatedEquipment);
    }
    throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
  }

  public void delete(Long id) {
    if (!equipmentRepository.existsById(id)) {
      throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
    }
    equipmentRepository.deleteById(id);
  }

  // Lấy danh sách thiết bị không khả dụng
  public List<EquipmentDTO> getUnavailableEquipments() {
    return equipmentRepository.findByAvailableFalse()
        .stream()
        .map(equipmentMapper::toDTO)
        .toList();
  }

  // Thống kê thiết bị theo trạng thái
  public Map<String, Long> getEquipmentStatistics() {
    long availableCount = equipmentRepository.countByAvailableTrue();
    long unavailableCount = equipmentRepository.countByAvailableFalse();
    return Map.of(
        "available", availableCount,
        "unavailable", unavailableCount
    );
  }
}
