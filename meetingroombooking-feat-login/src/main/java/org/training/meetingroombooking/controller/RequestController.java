package org.training.meetingroombooking.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.dto.ApiResponse;
import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.model.Request;
import org.training.meetingroombooking.service.RequestService;

@RestController
@RequestMapping("/request")
public class RequestController {

  @Autowired
  RequestService requestService;

  @PostMapping
  ApiResponse<RequestDTO> addRequest(@RequestBody RequestDTO requestDTO) {
    ApiResponse<RequestDTO> apiResponse = new ApiResponse<>();
    apiResponse.setData(requestService.createRequest(requestDTO));
    return apiResponse;
  }
  @GetMapping
  List<Request> getRequests() {
    return requestService.getAllRequests();
  }

  @GetMapping("/{requestId}")
  String deleteRequest(@PathVariable int requestId) {
    requestService.deleteRequest(requestId);
    return "Request deleted successfully";

  }
}
