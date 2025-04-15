package org.training.meetingroombooking.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.Set;
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
import org.training.meetingroombooking.repository.RoomRepository;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final RoomEquipmentRepository roomEquipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository,
                            EquipmentMapper equipmentMapper,
                            RoomEquipmentRepository roomEquipmentRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.roomEquipmentRepository = roomEquipmentRepository;
    }

    public EquipmentDTO create(EquipmentDTO equipmentDTO) {
        boolean exists = equipmentRepository.existsById(equipmentDTO.getEquipmentName());
        if (exists) {
            throw new AppEx(ErrorCode.EQUIPMENT_ALREADY_EXISTS);
        }
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

    public Page<EquipmentDTO> getEquipments(String equipmentName, String description, Boolean available,
        int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Equipment> spec = Specification.where(null);
        if (equipmentName != null && !equipmentName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("equipmentName")), "%" + equipmentName.toLowerCase() + "%"));
        }
        if (description != null && !description.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }
        if (available != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("available"), available));
        }
        Page<Equipment> equipmentPage = equipmentRepository.findAll(spec, pageable);
        return equipmentPage.map(equipmentMapper::toDTO);
    }

    public EquipmentDTO getById(String equipmentName) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentName);
        return equipment.map(equipmentMapper::toDTO).orElseThrow(
                () -> new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND));
    }

    public EquipmentDTO update(String equipmentName, EquipmentDTO equipmentDTO) {
        Optional<Equipment> existingEquipment = equipmentRepository.findById(equipmentName);
        if (existingEquipment.isPresent()) {
            Equipment equipment = existingEquipment.get();
            equipmentMapper.updateEntity(equipment, equipmentDTO);
            Equipment updatedEquipment = equipmentRepository.save(equipment);
            return equipmentMapper.toDTO(updatedEquipment);
        }
        throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
    }

    public void delete(String equipmentName) {
        if (!equipmentRepository.existsById(equipmentName)) {
            throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        equipmentRepository.deleteById(equipmentName);
    }

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
