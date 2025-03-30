//package org.training.meetingroombooking.service;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
//import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
//import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
//import org.training.meetingroombooking.entity.enums.ErrorCode;
//import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
//import org.training.meetingroombooking.exception.AppEx;
//import org.training.meetingroombooking.repository.RoomBookingRepository;
//import org.training.meetingroombooking.repository.RoomRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//class StatisticalServiceTest {
//
//    @Mock
//    private RoomBookingRepository roomBookingRepository;
//
//    @Mock
//    private RoomBookingMapper roomBookingMapper;
//    private RoomRepository roomRepository;
//    private StatisticalService statisticalService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        statisticalService = new StatisticalService(roomBookingRepository, roomBookingMapper, roomRepository);
//    }
//
//    @Test
//    void getMostBookedRoomOfMonth_ShouldReturnRoomSummaryDTO() {
//        // Arrange
//        LocalDate currentDate = LocalDate.now();
//        int month = currentDate.getMonthValue();
//        int year = currentDate.getYear();
//
//        Object[] roomBookingData = new Object[]{1L, 10L}; // Room ID and booking count
//        when(roomBookingRepository.findMostBookedRoomOfMonth(month, year))
//                .thenReturn(Optional.of(roomBookingData));
//
//        // Act
//        RoomSummaryDTO result = statisticalService.getMostBookedRoomOfMonth();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Most booked room of the month", result.getRoomName());
//        assertEquals(10L, result.getBookingCount());
//        verify(roomBookingRepository).findMostBookedRoomOfMonth(month, year);
//    }
//
//    @Test
//    void getMostBookedRoomOfMonth_ShouldThrowAppEx_WhenNoBookingsFound() {
//        // Arrange
//        LocalDate currentDate = LocalDate.now();
//        int month = currentDate.getMonthValue();
//        int year = currentDate.getYear();
//
//        when(roomBookingRepository.findMostBookedRoomOfMonth(month, year))
//                .thenReturn(Optional.empty());
//
//        // Act & Assert
//        AppEx exception = assertThrows(AppEx.class, () -> statisticalService.getMostBookedRoomOfMonth());
//        assertEquals(ErrorCode.ROOM_BOOKING_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    void getCurrentMonthBookingCount_ShouldReturnBookingCount() {
//        // Arrange
//        LocalDate currentDate = LocalDate.now();
//        int month = currentDate.getMonthValue();
//        int year = currentDate.getYear();
//        long expectedBookingCount = 25L;
//        when(roomBookingRepository.countBookingsByMonth(month, year)).thenReturn(expectedBookingCount);
//
//        // Act
//        long result = statisticalService.getCurrentMonthBookingCount();
//
//        // Assert
//        assertEquals(expectedBookingCount, result);
//        verify(roomBookingRepository).countBookingsByMonth(month, year);
//    }
//
//    @Test
//    void getTopUsers_ShouldReturnTopUsers() {
//        // Arrange
//        int limit = 5;
//        List<Object[]> userData = List.of(
//                new Object[]{1L, "user1", 10L},
//                new Object[]{2L, "user2", 15L}
//        );
//        when(roomBookingRepository.findTopUsers(limit)).thenReturn(userData);
//
//        // Act
//        List<UserSummaryDTO> result = statisticalService.getTopUsers(limit);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("user1", result.get(0).getUserName());
//        assertEquals(10L, result.get(0).getBookingCount());
//        verify(roomBookingRepository).findTopUsers(limit);
//    }
//
//    @Test
//    void getWeeklyBookingCount_ShouldReturnBookingCount() {
//        // Arrange
//        int week = 1;
//        int year = 2025;
//        long expectedBookingCount = 50L;
//        when(roomBookingRepository.countBookingsByWeek(week, year)).thenReturn(expectedBookingCount);
//
//        // Act
//        long result = statisticalService.getWeeklyBookingCount(week, year);
//
//        // Assert
//        assertEquals(expectedBookingCount, result);
//        verify(roomBookingRepository).countBookingsByWeek(week, year);
//    }
//
//    @Test
//    void getQuarterlyBookingCount_ShouldReturnBookingCount() {
//        // Arrange
//        int quarter = 1;
//        int year = 2025;
//        long expectedBookingCount = 100L;
//        when(roomBookingRepository.countBookingsByQuarter(quarter, year)).thenReturn(expectedBookingCount);
//
//        // Act
//        long result = statisticalService.getQuarterlyBookingCount(quarter, year);
//
//        // Assert
//        assertEquals(expectedBookingCount, result);
//        verify(roomBookingRepository).countBookingsByQuarter(quarter, year);
//    }
//
//    @Test
//    void getRoomStatistics_ShouldReturnZero_WhenRepositoryMethodsReturnZero() {
//        // Arrange
//        long totalRooms = 0L;
//        long availableRooms = 0L;
//        long unavailableRooms = 0L;
//
//        when(roomRepository.count()).thenReturn(totalRooms);
//        when(roomRepository.countByAvailable(true)).thenReturn(availableRooms);
//        when(roomRepository.countByAvailable(false)).thenReturn(unavailableRooms);
//
//        // Act
//        RoomStatisticsDTO result = statisticalService.getRoomStatistics();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(totalRooms, result.getTotalRooms());
//        assertEquals(availableRooms, result.getAvailableRooms());
//        assertEquals(unavailableRooms, result.getUnavailableRooms());
//
//        verify(roomRepository).count();
//        verify(roomRepository).countByAvailable(true);
//        verify(roomRepository).countByAvailable(false);
//    }
//
//}
