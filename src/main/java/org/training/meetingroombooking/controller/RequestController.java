package org.training.meetingroombooking.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.RequestService;

@RestController
@RequestMapping("/request")
public class RequestController {
  private final RequestService requestService;

  public RequestController(RequestService requestService) {
    this.requestService = requestService;
  }

  @GetMapping
  public ApiResponse<List<RequestDTO>> getRequests() {
    return ApiResponse.<List<RequestDTO>>builder()
        .success(true)
        .data(requestService.getAllRequests())
        .build();
  }

  @PostMapping
  public ApiResponse<RequestDTO> createRequest(@RequestBody RequestDTO request) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.createRequest(request))
        .build();
  }

  @GetMapping("/{requestId}")
  public ApiResponse<RequestDTO> getRequest(@PathVariable("requestId") int requestId) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.getRequestById(requestId))
        .build();
  }

  @PutMapping("/{requestId}")
  public ApiResponse<RequestDTO> updateRequest(@PathVariable int requestId, @RequestBody RequestDTO request) {
    return ApiResponse.<RequestDTO>builder()
        .success(true)
        .data(requestService.updateRequest(requestId, request))
        .build();
  }

  @DeleteMapping("/{requestId}")
  public ApiResponse<String> deleteRequest(@PathVariable("requestId") int requestId) {
    requestService.deleteRequest(requestId);
    return ApiResponse.<String>builder().success(true).data("User has been deleted").build();
  }

}
