package org.training.meetingroombooking.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.model.Request;
import org.training.meetingroombooking.repository.RequestRepository;


@Service
public class RequestService {
  @Autowired
  private RequestRepository requestRepository;

    public RequestDTO createRequest(RequestDTO requestDTO) {
      Request request = new Request();
      request.setTitle(requestDTO.getTitle());
      request.setLocation(requestDTO.getLocation());
      request.setDescription(requestDTO.getDescription());
      request.setJobLevel(requestDTO.getJobLevel());
      request.setStatus(requestDTO.isStatus());
      request.setApproval(requestDTO.getApproval());
      return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
      return requestRepository.findAll();
    }

    public Request getRequestById(int RequestId) {
      return requestRepository.findById(RequestId)
          .orElseThrow(()->new RuntimeException("Request not found"));
    }

    public Request updateRequest(RequestDTO requestDTO) {
      Request request = new Request();
      request.setTitle(requestDTO.getTitle());
      request.setLocation(requestDTO.getLocation());
      request.setDescription(requestDTO.getDescription());
      request.setJobLevel(requestDTO.getJobLevel());
      request.setStatus(requestDTO.isStatus());
      request.setApproval(requestDTO.getApproval());
      return requestRepository.save(request);
    }

    public String deleteRequest(int RequestId) {
      requestRepository.deleteById(RequestId);
      return "Request delete ";
    }
}
