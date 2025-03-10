package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RequestMapper;
import org.training.meetingroombooking.repository.RequestRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestService requestService;

    private Request request;
    private RequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        request = new Request();
        request.setRequestId(1);
        request.setTitle("Meeting");
        request.setLocation("Room A");
        request.setDescription("Project discussion");
        request.setJobLevel("Senior");
        request.setStatus(true);
        request.setApproval("Approved");
        request.setTarget(LocalDate.now());
        request.setOnboard(LocalDate.now().plusDays(7));
        request.setAction("Proceed");

        requestDTO = new RequestDTO();
        requestDTO.setTitle("Meeting");
        requestDTO.setLocation("Room A");
        requestDTO.setDescription("Project discussion");
        requestDTO.setJobLevel("Senior");
        requestDTO.setStatus(true);
        requestDTO.setApproval("Approved");
        requestDTO.setTarget(LocalDate.now());
        requestDTO.setOnboard(LocalDate.now().plusDays(7));
        requestDTO.setAction("Proceed");
    }

    @Test
    void ***REMOVED***GetAllRequestsPaged() {
        Page<Request> mockPage = new PageImpl<>(Collections.singletonList(request));
        when(requestRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        Page<RequestDTO> result = requestService.getAllRequestsPaged(0, 10, "title", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void ***REMOVED***GetRequestsByExactTitle() {
        when(requestRepository.findByTitle("Meeting")).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.getRequestsByExactTitle("Meeting");

        assertEquals(1, result.size());
        assertEquals("Meeting", result.get(0).getTitle());
    }

    @Test
    void ***REMOVED***SearchRequestsByTitle() {
        when(requestRepository.findByTitleContainingIgnoreCase("Meet")).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.searchRequestsByTitle("Meet");

        assertEquals(1, result.size());
    }

    @Test
    void ***REMOVED***SearchRequestsByTitlePaged() {
        Page<Request> mockPage = new PageImpl<>(Collections.singletonList(request));
        when(requestRepository.findByTitleContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mockPage);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        Page<RequestDTO> result = requestService.searchRequestsByTitlePaged("Meet", 0, 10, "title", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void ***REMOVED***CreateRequest() {
        when(requestMapper.toEntity(any(RequestDTO.class))).thenReturn(request);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        RequestDTO result = requestService.createRequest(requestDTO);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***GetRequestById_Found() {
        when(requestRepository.findById(1)).thenReturn(Optional.of(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        RequestDTO result = requestService.getRequestById(1);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***GetRequestById_NotFound() {
        when(requestRepository.findById(1)).thenReturn(Optional.empty());

        RequestDTO result = requestService.getRequestById(1);

        assertNull(result);
    }

    @Test
    void ***REMOVED***GetAllRequests() {
        when(requestRepository.findAll()).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.getAllRequests();

        assertEquals(1, result.size());
    }

    @Test
    void ***REMOVED***UpdateRequest_Found() {
        when(requestRepository.findById(1)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        RequestDTO result = requestService.updateRequest(1, requestDTO);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***UpdateRequest_NotFound() {
        when(requestRepository.findById(1)).thenReturn(Optional.empty());

        RequestDTO result = requestService.updateRequest(1, requestDTO);

        assertNull(result);
    }

    @Test
    void ***REMOVED***DeleteRequest_Success() {
        when(requestRepository.existsById(1)).thenReturn(true);
        doNothing().when(requestRepository).deleteById(1);

        assertDoesNotThrow(() -> requestService.deleteRequest(1));
    }

    @Test
    void ***REMOVED***DeleteRequest_NotFound() {
        when(requestRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(AppEx.class, () -> requestService.deleteRequest(1));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ((AppEx) exception).getErrorCode());
    }
}
