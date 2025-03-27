package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.***REMOVED***.context.SpringBootTest;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.EquipmentRepository;
import org.training.meetingroombooking.entity.mapper.EquipmentMapper;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentService equipmentService;

    private EquipmentDTO equipmentDTO;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipmentDTO = EquipmentDTO.builder()
                .name("Projector")
                .description("4K Projector")
                .quantity(5)
                .available(true)
                .build();

        equipment = Equipment.builder()
                .equipmentId(1L)
                .name("Projector")
                .description("4K Projector")
                .quantity(5)
                .available(true)
                .build();
    }

    @Test
    void ***REMOVED***Create() {
        // Arrange
        when(equipmentMapper.toEntity(equipmentDTO)).thenReturn(equipment);
        when(equipmentRepository.save(equipment)).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        // Act
        EquipmentDTO result = equipmentService.create(equipmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Projector", result.getName());
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void ***REMOVED***GetAll() {
        // Arrange
        List<Equipment> equipmentList = List.of(equipment);
        when(equipmentRepository.findAll()).thenReturn(equipmentList);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        // Act
        List<EquipmentDTO> result = equipmentService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Projector", result.get(0).getName());
    }

    @Test
    void ***REMOVED***GetById_Found() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        // Act
        EquipmentDTO result = equipmentService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Projector", result.getName());
    }

    @Test
    void ***REMOVED***GetById_NotFound() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        AppEx exception = assertThrows(AppEx.class, () -> equipmentService.getById(1L));
        assertEquals("EQUIPMENT_NOT_FOUND", exception.getErrorCode().name());
    }

    @Test
    void ***REMOVED***Update() {
        EquipmentDTO updatedDTO = EquipmentDTO.builder()
                .name("Updated Projector")
                .description("Updated Description")
                .quantity(10)
                .available(false)
                .build();

        Equipment updatedEntity = Equipment.builder()
                .equipmentId(1L)
                .name("Updated Projector")
                .description("Updated Description")
                .quantity(10)
                .available(false)
                .build();

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        doNothing().when(equipmentMapper).updateEntity(equipment, updatedDTO);

        when(equipmentRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(equipmentMapper.toDTO(updatedEntity)).thenReturn(updatedDTO);

        EquipmentDTO result = equipmentService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Projector", result.getName());
        verify(equipmentRepository, times(1)).save(updatedEntity);
    }


    @Test
    void ***REMOVED***Delete() {
        // Arrange
        when(equipmentRepository.existsById(1L)).thenReturn(true);

        // Act
        equipmentService.delete(1L);

        // Assert
        verify(equipmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void ***REMOVED***Delete_NotFound() {
        // Arrange
        when(equipmentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        AppEx exception = assertThrows(AppEx.class, () -> equipmentService.delete(1L));
        assertEquals("EQUIPMENT_NOT_FOUND", exception.getErrorCode().name());
    }
}
