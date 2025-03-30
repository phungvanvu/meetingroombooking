package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.EquipmentService;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

  private final EquipmentService equipmentService;

  public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO dto) {
    return ApiResponse.<EquipmentDTO>builder()
        .success(true)
        .data(equipmentService.create(dto))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<EquipmentDTO>> getEquipments() {
    return ApiResponse.<List<EquipmentDTO>>builder()
        .success(true)
        .data(equipmentService.getAll())
        .build();
  }

  @GetMapping("/distinct")
  public ApiResponse<Set<EquipmentDTO>> getAllDistinctEquipments() {
    Set<EquipmentDTO> distinctEquipments = equipmentService.getAllDistinctEquipments();
    return ApiResponse.<Set<EquipmentDTO>>builder()
            .success(true)
            .data(distinctEquipments)
            .build();
  }

  @GetMapping("/{equipmentName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<EquipmentDTO> getEquipmentById(@PathVariable String equipmentName) {
    return ApiResponse.<EquipmentDTO>builder()
        .success(true)
        .data(equipmentService.getById(equipmentName))
        .build();
  }


  @PutMapping("/{equipmentName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<EquipmentDTO> updateEquipment(@PathVariable String equipmentName,
      @Valid @RequestBody EquipmentDTO dto) {
    return ApiResponse.<EquipmentDTO>builder()
        .success(true)
        .data(equipmentService.update(equipmentName, dto))
        .build();
  }

  @DeleteMapping("/{equipmentName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteEquipment(@PathVariable String equipmentName) {
    equipmentService.delete(equipmentName);
    return ApiResponse.<String>builder()
        .success(true)
        .data("equipment has been deleted")
        .build();
  }

  @GetMapping("/unavailable")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<EquipmentDTO>> getUnavailableEquipments() {
    return ApiResponse.<List<EquipmentDTO>>builder()
        .success(true)
        .data(equipmentService.getUnavailableEquipments())
        .build();
  }

  @GetMapping("/statistics")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Map<String, Long>> getEquipmentStatistics() {
    return ApiResponse.<Map<String, Long>>builder()
        .success(true)
        .data(equipmentService.getEquipmentStatistics())
        .build();
  }

}
