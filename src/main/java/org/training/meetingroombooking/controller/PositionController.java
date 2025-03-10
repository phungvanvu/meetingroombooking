package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.dto.PositionDTO;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.PositionService;

@RestController
@RequestMapping("/position")
public class PositionController {
  private final PositionService positionService;
  public PositionController(PositionService positionService) {
    this.positionService = positionService;
  }
  @PostMapping
  public ApiResponse<PositionDTO> createPosition(@Valid @RequestBody PositionDTO positionDTO) {
    return ApiResponse.<PositionDTO>builder()
        .success(true)
        .data(positionService.create(positionDTO))
        .build();
  }
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<PositionDTO>> getAll() {
    return ApiResponse.<List<PositionDTO>>builder()
        .success(true)
        .data(positionService.getAll())
        .build();
  }
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> DeletePosition(@PathVariable int id) {
    positionService.deletePosition(id);
    return ApiResponse.<String>builder()
        .success(true)
        .data(" position has been deleted")
        .build();
  }

}
