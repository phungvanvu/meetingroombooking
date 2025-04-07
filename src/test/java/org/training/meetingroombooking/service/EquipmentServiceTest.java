package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.training.meetingroombooking.entity.dto.EquipmentDTO;
import org.training.meetingroombooking.entity.mapper.EquipmentMapper;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.EquipmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentServiceTest {

    @InjectMocks
    private EquipmentService equipmentService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    private Equipment equipment;
    private EquipmentDTO equipmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Equipment entity
        equipment = Equipment.builder()

                .equipmentName("Máy chiếu")
                .description("Máy chiếu 4K")
                .available(true)
                .build();

        // Mock DTO
        equipmentDTO = EquipmentDTO.builder()
                .equipmentName("Máy chiếu")
                .description("Máy chiếu 4K")
                .available(true)
                .build();
    }

    @Test
    void testCreateEquipment() {
        when(equipmentMapper.toEntity(equipmentDTO)).thenReturn(equipment);
        when(equipmentRepository.save(equipment)).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentService.create(equipmentDTO);

        assertNotNull(result);
        assertEquals("Máy chiếu", result.getEquipmentName());
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void testGetById_Found() {
        when(equipmentRepository.findById("Máy chiếu")).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentService.getById("Máy chiếu");

        assertNotNull(result);
        assertEquals("Máy chiếu", result.getEquipmentName());
        verify(equipmentRepository, times(1)).findById("Máy chiếu");
    }

    @Test
    void testGetById_NotFound() {
        when(equipmentRepository.findById("Máy chiếu")).thenReturn(Optional.empty());

        assertThrows(AppEx.class, () -> equipmentService.getById("Máy chiếu"));
        verify(equipmentRepository, times(1)).findById("Máy chiếu");
    }

    @Test
    void testUpdateEquipment_Found() {
        EquipmentDTO updateDTO = EquipmentDTO.builder()
                .equipmentName("Máy chiếu HD")
                .description("Máy chiếu Full HD")
                .available(false)
                .build();

        when(equipmentRepository.findById("Máy chiếu")).thenReturn(Optional.of(equipment));
        doNothing().when(equipmentMapper).updateEntity(equipment, updateDTO);
        when(equipmentRepository.save(equipment)).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(updateDTO);

        EquipmentDTO result = equipmentService.update("Máy chiếu", updateDTO);

        assertNotNull(result);
        assertEquals("Máy chiếu HD", result.getEquipmentName());
        assertEquals("Máy chiếu Full HD", result.getDescription());
        assertFalse(result.getAvailable());
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void testUpdateEquipment_NotFound() {
        when(equipmentRepository.findById("Máy chiếu")).thenReturn(Optional.empty());

        EquipmentDTO updateDTO = EquipmentDTO.builder()
                .equipmentName("Máy chiếu HD")
                .description("Máy chiếu Full HD")
                .available(false)
                .build();

        assertThrows(AppEx.class, () -> equipmentService.update("Máy chiếu", updateDTO));
        verify(equipmentRepository, times(1)).findById("Máy chiếu");
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void testDeleteEquipment_Found() {
        when(equipmentRepository.existsById("Máy chiếu")).thenReturn(true);
        doNothing().when(equipmentRepository).deleteById("Máy chiếu");

        assertDoesNotThrow(() -> equipmentService.delete("Máy chiếu"));
        verify(equipmentRepository, times(1)).deleteById("Máy chiếu");
    }

    @Test
    void testDeleteEquipment_NotFound() {
        when(equipmentRepository.existsById("Máy chiếu")).thenReturn(false);

        assertThrows(AppEx.class, () -> equipmentService.delete("Máy chiếu"));
        verify(equipmentRepository, times(1)).existsById("Máy chiếu");
        verify(equipmentRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetAllEquipments() {
        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        List<EquipmentDTO> result = equipmentService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Máy chiếu", result.get(0).getEquipmentName());
        verify(equipmentRepository, times(1)).findAll();
    }
}
