package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.GroupRepository;
import org.training.meetingroombooking.entity.mapper.GroupMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    private GroupDTO groupDTO;
    private GroupEntity groupEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        groupDTO = GroupDTO.builder()
                .groupName("Test Group")
                .location("Test Location")
                .division("Test Division")
                .department("Test Department")
                .createdDate(null)
                .build();

        groupEntity = GroupEntity.builder()
                .groupName("Test Group")
                .location("Test Location")
                .division("Test Division")
                .department("Test Department")
                .createdDate(null)
                .build();
    }

    @Test
    void testCreateGroup() {
        // Arrange
        when(groupMapper.toEntity(groupDTO)).thenReturn(groupEntity);
        when(groupRepository.save(groupEntity)).thenReturn(groupEntity);
        when(groupMapper.toDTO(groupEntity)).thenReturn(groupDTO);

        // Act
        GroupDTO result = groupService.create(groupDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Group", result.getGroupName());
        verify(groupRepository, times(1)).save(groupEntity);
    }

    @Test
    void testGetAllGroups() {
        // Arrange
        List<GroupEntity> groupEntities = List.of(groupEntity);
        when(groupRepository.findAll()).thenReturn(groupEntities);
        when(groupMapper.toDTO(groupEntity)).thenReturn(groupDTO);

        // Act
        List<GroupDTO> result = groupService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Group", result.get(0).getGroupName());
    }

    @Test
    void testGetGroupById() {
        // Arrange
        when(groupRepository.findById("Test Group")).thenReturn(Optional.of(groupEntity));
        when(groupMapper.toDTO(groupEntity)).thenReturn(groupDTO);

        // Act
        GroupDTO result = groupService.getById("Test Group");

        // Assert
        assertNotNull(result);
        assertEquals("Test Group", result.getGroupName());
    }

    @Test
    void testGetGroupByIdNotFound() {
        // Arrange
        when(groupRepository.findById("Non Existent Group")).thenReturn(Optional.empty());

        // Act & Assert
        AppEx exception = assertThrows(AppEx.class, () -> groupService.getById("Non Existent Group"));
        assertEquals("GROUP_NOT_FOUND", exception.getErrorCode().name());
    }

    @Test
    void testUpdateGroup() {
        // Arrange
        GroupDTO updatedGroupDTO = GroupDTO.builder()
                .groupName("Test Group")
                .location("Updated Location")
                .division("Updated Division")
                .department("Updated Department")
                .createdDate(null)
                .build();

        GroupEntity updatedGroupEntity = GroupEntity.builder()
                .groupName("Test Group")
                .location("Updated Location")
                .division("Updated Division")
                .department("Updated Department")
                .createdDate(null)
                .build();

        // Giả lập phương thức findById để trả về đối tượng hiện tại
        when(groupRepository.findById("Test Group")).thenReturn(Optional.of(groupEntity));

        // Giả lập phương thức updateEntity (nếu là void, ta không cần return)
        doNothing().when(groupMapper).updateEntity(groupEntity, updatedGroupDTO);

        when(groupRepository.save(updatedGroupEntity)).thenReturn(updatedGroupEntity);

        when(groupMapper.toDTO(updatedGroupEntity)).thenReturn(updatedGroupDTO);

        // Act
        GroupDTO result = groupService.update("Test Group", updatedGroupDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Location", result.getLocation());
        assertEquals("Updated Division", result.getDivision());
        verify(groupRepository, times(1)).save(updatedGroupEntity);
    }

    @Test
    void testDeleteGroup() {
        // Arrange
        when(groupRepository.existsById("Test Group")).thenReturn(true);

        // Act
        groupService.delete("Test Group");

        // Assert
        verify(groupRepository, times(1)).deleteById("Test Group");
    }

    @Test
    void testDeleteGroupNotFound() {
        // Arrange
        when(groupRepository.existsById("Non Existent Group")).thenReturn(false);

        // Act & Assert
        AppEx exception = assertThrows(AppEx.class, () -> groupService.delete("Non Existent Group"));
        assertEquals("GROUP_NOT_FOUND", exception.getErrorCode().name());
    }
}
