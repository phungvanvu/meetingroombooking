package org.training.meetingroombooking.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.RequestService;

@RestController
@RequestMapping("/request")
public class RequestController {
  private final RequestService requestService;

  public RequestController(RequestService requestService) {

    this.requestService = requestService;
  }
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/paged")
  public Page<RequestDTO> getRequestsPaged(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "requestId") String sortBy,
          @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    return requestService.getAllRequestsPaged(page, size, sortBy, sortDirection);
  }

  @GetMapping("/search/exact")
  public List<RequestDTO> getRequestsByExactTitle(@RequestParam String title) {
    return requestService.getRequestsByExactTitle(title);
  }

  @GetMapping("/search")
  public List<RequestDTO> searchRequestsByTitle(@RequestParam String keyword) {
    return requestService.searchRequestsByTitle(keyword);
  }

  @GetMapping("/search/paged")
  public Page<RequestDTO> searchRequestsByTitlePaged(
          @RequestParam String keyword,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size,
          @RequestParam(defaultValue = "title") String sortBy,
          @RequestParam(defaultValue = "asc") String sortDirection) {
    return requestService.searchRequestsByTitlePaged(keyword, page, size, sortBy, sortDirection);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RequestDTO>> getRequests() {
    return ApiResponse.<List<RequestDTO>>builder()
        .success(true)
        .data(requestService.getAllRequests())
        .build();
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
  public ApiResponse<RequestDTO> createRequest(@RequestBody RequestDTO request) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.createRequest(request))
        .build();
  }

  @GetMapping("/{requestId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RequestDTO> getRequest(@PathVariable("requestId") Long requestId) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.getRequestById(requestId))
        .build();
  }

  @PutMapping("/{requestId}")
  @PostAuthorize("hasRole('ADMIN')")
  public ApiResponse<RequestDTO> updateRequest(@PathVariable Long requestId, @RequestBody RequestDTO request) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.updateRequest(requestId, request))
        .build();
  }

  @DeleteMapping("/{requestId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRequest(@PathVariable("requestId") Long requestId) {
    requestService.deleteRequest(requestId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Request has been deleted")
        .build();
  }

}
