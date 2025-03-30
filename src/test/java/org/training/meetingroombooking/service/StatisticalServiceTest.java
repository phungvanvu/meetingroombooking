//package org.training.meetingroombooking.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO;
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
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class StatisticalServiceTest {
//
//    @Mock
//    private RoomBookingRepository roomBookingRepository;
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @InjectMocks
//    private StatisticalService statisticalService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        statisticalService = new StatisticalService(roomBookingRepository, roomRepository);
//    }
//
//    @Test
//    void getMostBookedRoom_ShouldReturnRoomSummaryDTO() {
//        // Sắp xếp
//        Object[] roomBookingData = new Object[] { 1L, "Phòng A", 10L }; // Room ID, Room Name, và số lượng đặt phòng
//        when(roomBookingRepository.findMostBookedRoom())
//                .thenReturn(roomBookingData);
//
//        // Hành động
//        RoomSummaryDTO result = statisticalService.getMostBookedRoom();
//
//        // Kiểm tra
//        assertNotNull(result);
//        assertEquals("Phòng A", result.getRoomName());
//        assertEquals(10L, result.getBookingCount());
//        verify(roomBookingRepository).findMostBookedRoom();
//    }
//
//    @Test
//    void getMostBookedRoom_ShouldReturnNull_WhenNoBookingsFound() {
//        // Sắp xếp
//        when(roomBookingRepository.findMostBookedRoom())
//                .thenReturn(null);
//
//        // Hành động
//        RoomSummaryDTO result = statisticalService.getMostBookedRoom();
//
//        // Kiểm tra
//        assertNull(result);
//        verify(roomBookingRepository).findMostBookedRoom();
//    }
//
//    @Test
//    void getCurrentMonthBookings_ShouldReturnBookingCount() {
//        // Sắp xếp
//        int currentMonth = LocalDate.now().getMonthValue();
//        long expectedBookingCount = 25L;
//        when(roomBookingRepository.countByMonth(currentMonth)).thenReturn(expectedBookingCount);
//
//        // Hành động
//        BookingSummaryDTO result = statisticalService.getCurrentMonthBookings();
//
//        // Kiểm tra
//        assertNotNull(result);
//        assertEquals(currentMonth, result.getPeriod());
//        assertEquals(expectedBookingCount, result.getBookings());
//        verify(roomBookingRepository).countByMonth(currentMonth);
//    }
//
//    @Test
//    void getWeeklyBookings_ShouldReturnBookingCount() {
//        // Sắp xếp
//        List<Object[]> weeklyBookings = Arrays.asList(new Object[]{1, 50L});
//        when(roomBookingRepository.findWeeklyBookings()).thenReturn(weeklyBookings);
//
//        // Hành động
//        List<BookingSummaryDTO> result = statisticalService.getWeeklyBookings();
//
//        // Kiểm tra
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(50L, result.get(0).getBookings());
//        verify(roomBookingRepository).findWeeklyBookings();
//    }
//
//    @Test
//    void getQuarterlyBookings_ShouldReturnBookingCount() {
//        // Sắp xếp
//        List<Object[]> quarterlyBookings = Arrays.asList(new Object[]{1, 100L});
//        when(roomBookingRepository.findQuarterlyBookings()).thenReturn(quarterlyBookings);
//
//        // Hành động
//        List<BookingSummaryDTO> result = statisticalService.getQuarterlyBookings();
//
//        // Kiểm tra
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(100L, result.get(0).getBookings());
//        verify(roomBookingRepository).findQuarterlyBookings();
//    }
//
//    @Test
//    void getRoomStatistics_ShouldReturnZero_WhenRepositoryMethodsReturnZero() {
//        // Sắp xếp
//        long totalRooms = 0L;
//        long availableRooms = 0L;
//        long unavailableRooms = 0L;
//        long totalBookings = 0L;
//        long todayBookings = 0L;
//
//        when(roomRepository.count()).thenReturn(totalRooms);
//        when(roomRepository.countByAvailable(true)).thenReturn(availableRooms);
//        when(roomRepository.countByAvailable(false)).thenReturn(unavailableRooms);
//        when(roomBookingRepository.count()).thenReturn(totalBookings);
//        when(roomBookingRepository.countByBookingDate(LocalDate.now())).thenReturn(todayBookings);
//
//        // Hành động
//        RoomStatisticsDTO result = statisticalService.getRoomStatistics();
//
//        // Kiểm tra
//        assertNotNull(result);
//        assertEquals(totalRooms, result.getTotalRooms());
//        assertEquals(availableRooms, result.getAvailableRooms());
//        assertEquals(unavailableRooms, result.getUnavailableRooms());
//        assertEquals(totalBookings, result.getTotalBookings());
//        assertEquals(todayBookings, result.getTodayBookings());
//
//        verify(roomRepository).count();
//        verify(roomRepository).countByAvailable(true);
//        verify(roomRepository).countByAvailable(false);
//        verify(roomBookingRepository).count();
//        verify(roomBookingRepository).countByBookingDate(LocalDate.now());
//    }
//}