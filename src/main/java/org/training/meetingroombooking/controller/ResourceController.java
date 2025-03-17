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
import org.training.meetingroombooking.entity.dto.ResourceDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.ResourceService;

@RestController
@RequestMapping("/resource")
public class ResourceController {

  private final ResourceService resourceService;

  public ResourceController(ResourceService resourceService){
    this.resourceService = resourceService;
  }

//  @PostMapping
//  @PreAuthorize("hasRole('ADMIN')")
//  public ApiResponse<ResourceDTO> createResource(@Valid @RequestBody ResourceDTO dto) {
//    return ApiResponse.<ResourceDTO>builder()
//        .success(true)
//        .data(resourceService.create(dto))
//        .build();
//  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<ResourceDTO>> getResources() {
    return ApiResponse.<List<ResourceDTO>>builder()
        .success(true)
        .data(resourceService.getAll())
        .build();
  }
  @GetMapping("/{resourceId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<ResourceDTO> getResource(@PathVariable Long resourceId) {
    return ApiResponse.<ResourceDTO>builder()
        .success(true)
        .data(resourceService.getById(resourceId))
        .build();
  }

  @PutMapping("/{resourceId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<ResourceDTO> updateResource(@PathVariable Long resourceId, @Valid @RequestBody ResourceDTO dto) {
    return ApiResponse.<ResourceDTO>builder()
        .success(true)
        .data(resourceService.update(resourceId, dto))
        .build();
  }
  @DeleteMapping("/{resourceId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteResource(@PathVariable Long resourceId) {
    return ApiResponse.<String>builder()
        .success(true)
        .data("resource has been deleted")
        .build();
  }
}
