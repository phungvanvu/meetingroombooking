package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
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
  public ApiResponse<List<EquipmentDTO>> getEquipments(){
    return ApiResponse.<List<EquipmentDTO>>builder()
        .success(true)
        .data(equipmentService.getAll())
        .build();
  }

  @PutMapping("/{equipmentId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<EquipmentDTO> updateEquipment(@PathVariable Long equipmentId, @Valid @RequestBody EquipmentDTO dto) {
    return ApiResponse.<EquipmentDTO>builder()
        .success(true)
        .data(equipmentService.update(equipmentId, dto))
        .build();
  }

  @DeleteMapping("/{equipmentId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteEquipment(@PathVariable Long equipmentId) {
    return ApiResponse.<String>builder()
        .success(true)
        .data("equipment has been deleted")
        .build();
  }
}
