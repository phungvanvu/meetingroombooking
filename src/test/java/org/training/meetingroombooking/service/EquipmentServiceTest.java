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
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentService.getById(1L);

        assertNotNull(result);
        assertEquals("Máy chiếu", result.getEquipmentName());
        verify(equipmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppEx.class, () -> equipmentService.getById(1L));
        verify(equipmentRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEquipment_Found() {
        EquipmentDTO updateDTO = EquipmentDTO.builder()
                .equipmentName("Máy chiếu HD")
                .description("Máy chiếu Full HD")
                .available(false)
                .build();

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        doNothing().when(equipmentMapper).updateEntity(equipment, updateDTO);
        when(equipmentRepository.save(equipment)).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(updateDTO);

        EquipmentDTO result = equipmentService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Máy chiếu HD", result.getEquipmentName());
        assertEquals("Máy chiếu Full HD", result.getDescription());
        assertFalse(result.getAvailable());
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void testUpdateEquipment_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        EquipmentDTO updateDTO = EquipmentDTO.builder()
                .equipmentName("Máy chiếu HD")
                .description("Máy chiếu Full HD")
                .available(false)
                .build();

        assertThrows(AppEx.class, () -> equipmentService.update(1L, updateDTO));
        verify(equipmentRepository, times(1)).findById(1L);
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void testDeleteEquipment_Found() {
        when(equipmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(equipmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> equipmentService.delete(1L));
        verify(equipmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEquipment_NotFound() {
        when(equipmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(AppEx.class, () -> equipmentService.delete(1L));
        verify(equipmentRepository, times(1)).existsById(1L);
        verify(equipmentRepository, never()).deleteById(anyLong());
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
