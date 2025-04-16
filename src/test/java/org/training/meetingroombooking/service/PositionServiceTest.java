package org.training.meetingroombooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.mapper.PositionMapper;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PositionRepository;

class PositionServiceTest {

  @Mock private PositionRepository positionRepository;

  @Mock private PositionMapper positionMapper;

  @InjectMocks private PositionService positionService;

  private Position position;
  private PositionDTO positionDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    position = Position.builder().positionName("Manager").description("Manage team").build();

    positionDTO = PositionDTO.builder().positionName("Manager").description("Manage team").build();
  }

  @Test
  void ***REMOVED***CreatePosition() {
    when(positionMapper.toEntity(positionDTO)).thenReturn(position);
    when(positionRepository.save(position)).thenReturn(position);
    when(positionMapper.toDTO(position)).thenReturn(positionDTO);

    PositionDTO result = positionService.create(positionDTO);

    assertNotNull(result);
    assertEquals("Manager", result.getPositionName());
    verify(positionRepository, times(1)).save(position);
  }

  @Test
  void ***REMOVED***GetAllPositions() {
    when(positionRepository.findAll()).thenReturn(List.of(position));
    when(positionMapper.toDTO(position)).thenReturn(positionDTO);

    List<PositionDTO> result = positionService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Manager", result.get(0).getPositionName());
  }

  @Test
  void ***REMOVED***UpdatePosition() {
    when(positionRepository.findById("Manager")).thenReturn(Optional.of(position));

    doNothing().when(positionMapper).updatePosition(position, positionDTO);

    when(positionRepository.save(position)).thenReturn(position);
    when(positionMapper.toDTO(position)).thenReturn(positionDTO);

    PositionDTO result = positionService.update("Manager", positionDTO);

    assertNotNull(result);
    assertEquals("Manager", result.getPositionName());
    verify(positionRepository, times(1)).save(position);
  }

  @Test
  void ***REMOVED***UpdatePositionNotFound() {
    when(positionRepository.findById("Manager")).thenReturn(Optional.empty());

    assertThrows(AppEx.class, () -> positionService.update("Manager", positionDTO));
  }

  @Test
  void ***REMOVED***DeletePosition() {
    when(positionRepository.existsById("Manager")).thenReturn(true);

    positionService.deletePosition("Manager");

    verify(positionRepository, times(1)).deleteById("Manager");
  }

  @Test
  void ***REMOVED***DeletePositionNotFound() {
    when(positionRepository.existsById("Manager")).thenReturn(false);

    assertThrows(AppEx.class, () -> positionService.deletePosition("Manager"));
  }
}
