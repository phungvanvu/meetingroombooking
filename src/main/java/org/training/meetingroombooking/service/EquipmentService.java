package org.training.meetingroombooking.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;

public interface EquipmentService {
  EquipmentDTO create(EquipmentDTO equipmentDTO);

  List<EquipmentDTO> getAll();

  Page<EquipmentDTO> getEquipments(
      String equipmentName,
      String description,
      Boolean available,
      int page,
      int size,
      String sortBy,
      String sortDirection);

  EquipmentDTO getById(String equipmentName);

  EquipmentDTO update(String equipmentName, EquipmentDTO equipmentDTO);

  void delete(String equipmentName);

  void deleteMultipleEquipments(List<String> equipmentNames);
}
