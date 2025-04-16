package org.training.meetingroombooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;

class RoomBookingServiceTest {

  @Mock private RoomBookingRepository roomBookingRepository;

  @Mock private RoomBookingMapper roomBookingMapper;

  @Mock private EmailService emailService;

  @Mock private RoomRepository roomRepository;

  @InjectMocks private RoomBookingService roomBookingService;

  private RoomBookingDTO roomBookingDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    roomBookingDTO = new RoomBookingDTO();
    roomBookingDTO.setRoomId(1L);
    roomBookingDTO.setStartTime(LocalDateTime.now());
    roomBookingDTO.setEndTime(LocalDateTime.now().plusHours(1));
    roomBookingDTO.setUserName("user1");
  }

  @Test
  void create_ShouldCreateBooking_WhenNoOverlap() {
    // Arrange
    when(roomBookingRepository.existsByRoomAndTimeOverlap(anyLong(), any(), any()))
        .thenReturn(false);
    RoomBooking roomBooking = new RoomBooking();
    when(roomBookingMapper.toEntity(any())).thenReturn(roomBooking);
    RoomBooking savedRoomBooking = new RoomBooking();
    when(roomBookingRepository.save(any())).thenReturn(savedRoomBooking);
    when(roomBookingMapper.toDTO(any())).thenReturn(roomBookingDTO);

    // Act
    RoomBookingDTO result = roomBookingService.create(roomBookingDTO);

    // Assert
    assertNotNull(result);
    verify(roomBookingRepository).save(any());
    verify(emailService).sendRoomBookingConfirmationEmail(any());
  }

  @Test
  void create_ShouldThrowException_WhenOverlapExists() {
    // Arrange
    when(roomBookingRepository.existsByRoomAndTimeOverlap(anyLong(), any(), any()))
        .thenReturn(true);

    // Act & Assert
    assertThrows(AppEx.class, () -> roomBookingService.create(roomBookingDTO));
  }

  @Test
  void getAll_ShouldReturnBookings() {
    // Arrange
    RoomBooking roomBooking = new RoomBooking();
    roomBooking.setRoom(new Room());
    roomBooking.setStartTime(LocalDateTime.now());
    roomBooking.setEndTime(LocalDateTime.now().plusHours(1));

    RoomBookingDTO roomBookingDTO = new RoomBookingDTO();
    when(roomBookingRepository.findAll()).thenReturn(List.of(roomBooking));
    when(roomBookingMapper.toDTO(roomBooking)).thenReturn(roomBookingDTO);

    // Act
    List<RoomBookingDTO> result = roomBookingService.getAll();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(roomBookingRepository).findAll();
  }

  @Test
  void getBookingsByUserName_ShouldReturnUserBookings() {
    // Arrange
    User user =
        User.builder()
            .userName("user1")
            .fullName("John Doe")
            .email("user1@example.com")
            .password("password")
            .enabled(true)
            .build();

    RoomBooking roomBooking = new RoomBooking();
    roomBooking.setBookedBy(user);

    RoomBookingDTO roomBookingDTO = new RoomBookingDTO();
    when(roomBookingRepository.findByBookedBy_UserName("user1")).thenReturn(List.of(roomBooking));
    when(roomBookingMapper.toDTO(roomBooking)).thenReturn(roomBookingDTO);

    // Act
    List<RoomBookingDTO> result = roomBookingService.getBookingsByUserName("user1");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(roomBookingRepository).findByBookedBy_UserName("user1");
  }

  @Test
  void delete_ShouldDeleteRoomBooking_WhenBookingExists() {
    // Arrange
    when(roomBookingRepository.existsById(1L)).thenReturn(true);

    // Act
    roomBookingService.delete(1L);

    // Assert
    verify(roomBookingRepository).deleteById(1L);
  }

  @Test
  void delete_ShouldThrowException_WhenBookingNotFound() {
    // Arrange
    when(roomBookingRepository.existsById(1L)).thenReturn(false);

    // Act & Assert
    assertThrows(AppEx.class, () -> roomBookingService.delete(1L));
  }

  @Test
  void exportBookingsToExcel_ShouldReturnByteArrayOutputStream() throws IOException {
    // Arrange
    RoomBooking roomBooking = new RoomBooking();
    RoomBookingDTO roomBookingDTO = new RoomBookingDTO();
    roomBookingDTO.setRoomId(1L);
    roomBookingDTO.setRoomName("Room A");
    roomBookingDTO.setUserName("Test User");
    roomBookingDTO.setStartTime(LocalDateTime.now());
    roomBookingDTO.setEndTime(LocalDateTime.now().plusHours(1));
    roomBookingDTO.setStatus(BookingStatus.CONFIRMED);
    roomBookingDTO.setDescription("Test Description");

    when(roomBookingRepository.findAll()).thenReturn(List.of(roomBooking));
    when(roomBookingMapper.toDTO(roomBooking)).thenReturn(roomBookingDTO);

    // Act
    ByteArrayOutputStream result = roomBookingService.exportBookingsToExcel();

    // Assert
    assertNotNull(result);
    assertTrue(result.size() > 0);
  }
}
