package org.training.meetingroombooking.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.entity.Request;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.exception.ErrorCode;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public RequestDTO createRequest(RequestDTO requestDTO) {
        Request request = requestMapper.toEntity(requestDTO);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDTO(savedRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RequestDTO getRequestById(int id) {
        Optional<Request> request = requestRepository.findById(id);
        return request.map(requestMapper::toDTO).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RequestDTO> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(requestMapper::toDTO)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRequest(int requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        requestRepository.deleteById(requestId);
    }
}
