package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.enums.RequestStatus;
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
        request = Request.builder()
                .requestId(1L)
                .title("Meeting")
                .location("Room A")
                .description("Project discussion")
                .jobLevel("Senior")
                .status(RequestStatus.APPROVED)
                .approval("Approved")
                .target(LocalDate.now().plusDays(1))
                .onboard(LocalDate.now().plusDays(7))
                .createdBy(null)
                .hrPic(null)
                .action("Proceed")
                .build();

        requestDTO = RequestDTO.builder()
                .title("Meeting")
                .location("Room A")
                .description("Project discussion")
                .jobLevel("Senior")
                .status(RequestStatus.APPROVED)
                .approval("Approved")
                .target(LocalDate.now().plusDays(1))
                .onboard(LocalDate.now().plusDays(7))
                .createdBy(1L)
                .hrPic(2L)
                .action("Proceed")
                .build();
    }

    @Test
    void ***REMOVED***GetAllRequestsPaged() {
        Page<Request> mockPage = new PageImpl<>(Collections.singletonList(request));
        when(requestRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        Page<RequestDTO> result = requestService.getAllPaged(0, 10, "title", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Meeting", result.getContent().get(0).getTitle());
    }

    @Test
    void ***REMOVED***GetRequestsByExactTitle() {
        when(requestRepository.findByTitle("Meeting")).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.getByExactTitle("Meeting");

        assertEquals(1, result.size());
        assertEquals("Meeting", result.get(0).getTitle());
    }

    @Test
    void ***REMOVED***SearchRequestsByTitle() {
        when(requestRepository.findByTitleContainingIgnoreCase("Meet")).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.searchByTitle("Meet");

        assertEquals(1, result.size());
        assertEquals("Meeting", result.get(0).getTitle());
    }

    @Test
    void ***REMOVED***SearchRequestsByTitlePaged() {
        Page<Request> mockPage = new PageImpl<>(Collections.singletonList(request));
        when(requestRepository.findByTitleContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mockPage);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        Page<RequestDTO> result = requestService.searchByTitlePaged("Meet", 0, 10, "title", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Meeting", result.getContent().get(0).getTitle());
    }

    @Test
    void ***REMOVED***CreateRequest() {
        when(requestMapper.toEntity(any(RequestDTO.class))).thenReturn(request);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestMapper.toDTO(any(Request.class))).thenReturn(requestDTO);

        RequestDTO result = requestService.create(requestDTO);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***GetRequestById_Found() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        RequestDTO result = requestService.getById(1L);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***GetRequestById_NotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        RequestDTO result = requestService.getById(1L);

        assertNull(result);
    }

    @Test
    void ***REMOVED***GetAllRequests() {
        when(requestRepository.findAll()).thenReturn(Collections.singletonList(request));
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        List<RequestDTO> result = requestService.getAll();

        assertEquals(1, result.size());
        assertEquals("Meeting", result.get(0).getTitle());
    }

    @Test
    void ***REMOVED***UpdateRequest_Found() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(requestDTO);

        RequestDTO result = requestService.update(1L, requestDTO);

        assertNotNull(result);
        assertEquals("Meeting", result.getTitle());
    }

    @Test
    void ***REMOVED***UpdateRequest_NotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        RequestDTO result = requestService.update(1L, requestDTO);

        assertNull(result);
    }

    @Test
    void ***REMOVED***DeleteRequest_Success() {
        when(requestRepository.existsById(1L)).thenReturn(true);
        doNothing().when(requestRepository).deleteById(1L);

        assertDoesNotThrow(() -> requestService.deleteRequest(1L));
    }

    @Test
    void ***REMOVED***DeleteRequest_NotFound() {
        when(requestRepository.existsById(1L)).thenReturn(false);

        AppEx exception = assertThrows(AppEx.class, () -> requestService.deleteRequest(1L));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }
}
