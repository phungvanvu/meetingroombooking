package org.training.meetingroombooking.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.EquipmentMapper;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.EquipmentRepository;
import org.training.meetingroombooking.repository.RoomEquipmentRepository;
import org.training.meetingroombooking.service.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;
  private final RoomEquipmentRepository roomEquipmentRepository;

  public EquipmentServiceImpl(
      EquipmentRepository equipmentRepository,
      EquipmentMapper equipmentMapper,
      RoomEquipmentRepository roomEquipmentRepository) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
    this.roomEquipmentRepository = roomEquipmentRepository;
  }

  private Sort getSort(String sortBy, String sortDirection) {
    return sortDirection.equalsIgnoreCase("DESC")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();
  }

  private Specification<Equipment> createSearchSpecification(
      String equipmentName, String description, Boolean available) {
    Specification<Equipment> spec = Specification.where(null);
    if (equipmentName != null && !equipmentName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(
                      cb.lower(root.get("equipmentName")),
                      "%" + equipmentName.toLowerCase() + "%"));
    }
    if (description != null && !description.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(
                      cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
    }
    if (available != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("available"), available));
    }
    return spec;
  }

  private void checkEquipmentExistence(String equipmentName) {
    if (!equipmentRepository.existsById(equipmentName)) {
      throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
    }
  }

  @Override
  public EquipmentDTO create(EquipmentDTO equipmentDTO) {
    if (equipmentRepository.existsById(equipmentDTO.getEquipmentName())) {
      throw new AppEx(ErrorCode.EQUIPMENT_ALREADY_EXISTS);
    }
    Equipment entity = equipmentMapper.toEntity(equipmentDTO);
    Equipment savedEquipment = equipmentRepository.save(entity);
    return equipmentMapper.toDTO(savedEquipment);
  }

  @Override
  public List<EquipmentDTO> getAll() {
    return equipmentRepository.findAll().stream().map(equipmentMapper::toDTO).toList();
  }

  @Override
  public Page<EquipmentDTO> getEquipments(
      String equipmentName,
      String description,
      Boolean available,
      int page,
      int size,
      String sortBy,
      String sortDirection) {
    Pageable pageable = PageRequest.of(page, size, getSort(sortBy, sortDirection));
    Specification<Equipment> spec =
        createSearchSpecification(equipmentName, description, available);
    Page<Equipment> equipmentPage = equipmentRepository.findAll(spec, pageable);
    return equipmentPage.map(equipmentMapper::toDTO);
  }

  @Override
  public EquipmentDTO getById(String equipmentName) {
    checkEquipmentExistence(equipmentName);
    return equipmentRepository
        .findById(equipmentName)
        .map(equipmentMapper::toDTO)
        .orElseThrow(() -> new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND));
  }

  @Override
  public EquipmentDTO update(String equipmentName, EquipmentDTO equipmentDTO) {
    checkEquipmentExistence(equipmentName);
    Equipment existingEquipment = equipmentRepository.findById(equipmentName).get();
    equipmentMapper.updateEntity(existingEquipment, equipmentDTO);
    Equipment updatedEquipment = equipmentRepository.save(existingEquipment);
    return equipmentMapper.toDTO(updatedEquipment);
  }

  @Override
  public void delete(String equipmentName) {
    checkEquipmentExistence(equipmentName);
    equipmentRepository.deleteById(equipmentName);
  }

  @Override
  public void deleteMultipleEquipments(List<String> equipmentNames) {
    if (equipmentNames == null || equipmentNames.isEmpty()) {
      throw new AppEx(ErrorCode.INVALID_INPUT);
    }
    List<Equipment> equipments = equipmentRepository.findAllById(equipmentNames);
    if (equipments.size() != equipmentNames.size()) {
      throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
    }
    for (Equipment equipment : equipments) {
      if (roomEquipmentRepository.existsByEquipment_EquipmentName(equipment.getEquipmentName())) {
        throw new AppEx(ErrorCode.CANNOT_DELETE_EQUIPMENT_IN_USE);
      }
    }
    equipmentRepository.deleteAll(equipments);
  }
}
