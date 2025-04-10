package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.GroupService;

@RestController
@RequestMapping("/group")
public class GroupController {

  private final GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<GroupDTO> create(@Valid @RequestBody GroupDTO request) {
    return ApiResponse.<GroupDTO>builder()
        .success(true)
        .data(groupService.create(request))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<GroupDTO>> getGroups() {
    return ApiResponse.<List<GroupDTO>>builder()
        .success(true)
        .data(groupService.getAll())
        .build();
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Page<GroupDTO>> searchGroups(
          @RequestParam(value = "groupName", required = false) String groupName,
          @RequestParam(value = "location", required = false) String location,
          @RequestParam(value = "division", required = false) String division,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "sortBy", defaultValue = "groupName") String sortBy,
          @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
  ) {
    Page<GroupDTO> pageResult = groupService.getGroups(groupName, location, division, page, size, sortBy, sortDirection);
    return ApiResponse.<Page<GroupDTO>>builder()
            .success(true)
            .data(pageResult)
            .build();
  }

  @GetMapping("/{groupName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<GroupDTO> getById(@PathVariable("groupName") String groupName) {
    return ApiResponse.<GroupDTO>builder()
        .success(true)
        .data(groupService.getById(groupName))
        .build();
  }

  @PutMapping("/{groupName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<GroupDTO> update(@PathVariable("groupName") String groupName,
      @Valid @RequestBody GroupDTO request) {
    return ApiResponse.<GroupDTO>builder()
        .success(true)
        .data(groupService.update(groupName, request))
        .build();
  }

  @DeleteMapping("/{groupName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> delete(@PathVariable("groupName") String groupName) {
    groupService.delete(groupName);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Group has been deleted successfully")
        .build();
  }

  @DeleteMapping("/delete-multiple")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<String> deleteMultipleGroups(@RequestBody List<String> groupNames) {
    groupService.deleteMultipleGroups(groupNames);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Selected groups have been deleted successfully.")
        .build();
  }
}
