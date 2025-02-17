package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.entity.Request;
import org.training.meetingroombooking.mapper.RequestMapper;
import org.training.meetingroombooking.repository.RequestRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public RequestDTO createRequest(RequestDTO requestDTO) {
        Request request = requestMapper.toEntity(requestDTO);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDTO(savedRequest);
    }

    public RequestDTO getRequestById(int id) {
        Optional<Request> request = requestRepository.findById(id);
        return request.map(requestMapper::toDTO).orElse(null);
    }

    public List<RequestDTO> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(requestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RequestDTO updateRequest(int id, RequestDTO requestDTO) {
        Optional<Request> existingRequest = requestRepository.findById(id);
        if (existingRequest.isPresent()) {
            Request request = existingRequest.get();
            request.setTitle(requestDTO.getTitle());
            request.setLocation(requestDTO.getLocation());
            request.setDescription(requestDTO.getDescription());
            request.setJobLevel(requestDTO.getJobLevel());
            request.setStatus(requestDTO.isStatus());
            request.setApproval(requestDTO.getApproval());
            request.setTarget(requestDTO.getTarget());
            request.setOnboard(requestDTO.getOnboard());
            request.setAction(requestDTO.getAction());

            Request updatedRequest = requestRepository.save(request);
            return requestMapper.toDTO(updatedRequest);
        }
        return null;
    }

    public void deleteRequest(int id) {
        requestRepository.deleteById(id);
    }
}
