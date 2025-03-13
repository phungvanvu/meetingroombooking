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
  public ApiResponse<List<GroupDTO>> getGroups(@Valid @RequestBody GroupDTO request) {
    return ApiResponse.<List<GroupDTO>>builder()
        .success(true)
        .data(groupService.getAll())
        .build();
  }
  @DeleteMapping("/{groupName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> delete(@PathVariable("groupName") String groupName) {
    groupService.delete(groupName);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Group has been deleted")
        .build();
  }
}
