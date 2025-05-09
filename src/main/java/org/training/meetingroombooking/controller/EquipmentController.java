package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.EquipmentService;

@RestController
@RequestMapping("/api/v1/equipment")
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
  public ApiResponse<List<EquipmentDTO>> getEquipments() {
    return ApiResponse.<List<EquipmentDTO>>builder()
        .success(true)
        .data(equipmentService.getAll())
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
  public ApiResponse<EquipmentDTO> updateEquipment(
      @PathVariable String equipmentName, @Valid @RequestBody EquipmentDTO dto) {
    return ApiResponse.<EquipmentDTO>builder()
        .success(true)
        .data(equipmentService.update(equipmentName, dto))
        .build();
  }

  @DeleteMapping("/{equipmentName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteEquipment(@PathVariable String equipmentName) {
    equipmentService.delete(equipmentName);
    return ApiResponse.<String>builder().success(true).data("equipment has been deleted").build();
  }

  @DeleteMapping("/delete-multiple")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteMultipleEquipments(@RequestBody List<String> equipmentNames) {
    equipmentService.deleteMultipleEquipments(equipmentNames);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Selected equipments have been deleted successfully.")
        .build();
  }

  // Endpoint: tìm kiếm, phân trang và sắp xếp Equipment
  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Page<EquipmentDTO>> searchEquipments(
      @RequestParam(required = false) String equipmentName,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) Boolean available,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "equipmentName") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection) {
    Page<EquipmentDTO> result =
        equipmentService.getEquipments(
            equipmentName, description, available, page, size, sortBy, sortDirection);
    return ApiResponse.<Page<EquipmentDTO>>builder().success(true).data(result).build();
  }
}
