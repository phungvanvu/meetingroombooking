package org.training.meetingroombooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RequestMapper;
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

    public Page<RequestDTO> getAllRequestsPaged(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Request> requestPage = requestRepository.findAll(pageable);
        return requestPage.map(requestMapper::toDTO);
    }
    public List<RequestDTO> getRequestsByExactTitle(String title) {
        List<Request> requests = requestRepository.findByTitle(title);
        return requests.stream().map(requestMapper::toDTO).collect(Collectors.toList());
    }

    public List<RequestDTO> searchRequestsByTitle(String keyword) {
        List<Request> requests = requestRepository.findByTitleContainingIgnoreCase(keyword);
        return requests.stream().map(requestMapper::toDTO).collect(Collectors.toList());
    }

    public Page<RequestDTO> searchRequestsByTitlePaged(String keyword, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Request> requestPage = requestRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return requestPage.map(requestMapper::toDTO);
    }

    public RequestDTO createRequest(RequestDTO requestDTO) {
        Request request = requestMapper.toEntity(requestDTO);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDTO(savedRequest);
    }

    public RequestDTO getRequestById(Long id) {
        Optional<Request> request = requestRepository.findById(id);
        return request.map(requestMapper::toDTO).orElse(null);
    }

    public List<RequestDTO> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(requestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RequestDTO updateRequest(Long id, RequestDTO requestDTO) {
        Optional<Request> existingRequest = requestRepository.findById(id);
        if (existingRequest.isPresent()) {
            Request request = existingRequest.get();
            request.setTitle(requestDTO.getTitle());
            request.setLocation(requestDTO.getLocation());
            request.setDescription(requestDTO.getDescription());
            request.setJobLevel(requestDTO.getJobLevel());
            request.setStatus(requestDTO.getStatus());
            request.setApproval(requestDTO.getApproval());
            request.setTarget(requestDTO.getTarget());
            request.setOnboard(requestDTO.getOnboard());
            request.setAction(requestDTO.getAction());

            Request updatedRequest = requestRepository.save(request);
            return requestMapper.toDTO(updatedRequest);
        }
        return null;
    }

    public void deleteRequest(Long requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        requestRepository.deleteById(requestId);
    }
}
