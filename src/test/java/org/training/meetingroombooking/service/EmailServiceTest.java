package org.training.meetingroombooking.service;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.Purpose;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailService emailService;

    private RoomBookingDTO roomBookingDTO;
    private Room room;
    private User user;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomName("Room A");
        room.setRoomId(1L);

        user = new User();
        user.setEmail("***REMOVED***user@example.com");
        user.setFullName("Test User");
        user.setUserId(1L);

        roomBookingDTO = new RoomBookingDTO();
        roomBookingDTO.setRoomId(1L);
        roomBookingDTO.setBookedById(1L);
        roomBookingDTO.setPurpose(Purpose.MEETING);
        roomBookingDTO.setStartTime(LocalDateTime.parse("2025-03-28T10:00:00"));
        roomBookingDTO.setEndTime(LocalDateTime.parse("2025-03-28T12:00:00"));
        roomBookingDTO.setStatus(BookingStatus.CONFIRMED);
        roomBookingDTO.setDescription("Team discussion on project progress");
        roomBookingDTO.setCreatedAt(LocalDateTime.parse("2025-03-28T09:00:00"));
    }

    @Test
    void ***REMOVED***SendRoomBookingConfirmationEmail() throws MessagingException, IOException {
        // Arrange: mock dữ liệu
        Room room = new Room();
        room.setRoomName("Room A");

        User user = new User();
        user.setEmail("***REMOVED***user@example.com");
        user.setFullName("Test User");

        RoomBookingDTO roomBookingDTO = new RoomBookingDTO();
        roomBookingDTO.setRoomId(1L);
        roomBookingDTO.setBookedById(1L);

        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        // Act: tạo MimeMessage mock
        MimeMessage mimeMessage = mock(MimeMessage.class);

        // Mock lại hành vi của MimeMessage
        Address[] addresses = new Address[] { new InternetAddress("***REMOVED***user@example.com") };
        when(mimeMessage.getRecipients(MimeMessage.RecipientType.TO)).thenReturn(addresses);
        when(mimeMessage.getSubject()).thenReturn("Meeting room booking confirmation successful");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage); // Mock lại hành động tạo MimeMessage

        // Gửi email
        emailService.sendRoomBookingConfirmationEmail(roomBookingDTO);

        // Verify: xác minh gọi đúng số lần
        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(mimeMessageCaptor.capture());

        // Lấy MimeMessage đã được gửi
        MimeMessage capturedMessage = mimeMessageCaptor.getValue();

        // Kiểm tra thông tin trong MimeMessage
        assertEquals("***REMOVED***user@example.com", capturedMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("Meeting room booking confirmation successful", capturedMessage.getSubject());

        // Xử lý nội dung của email, có thể ném IOException
        String content = (String) capturedMessage.getContent();
        assertTrue(content.contains("You have successfully booked a meeting room!"));
    }

    @Test
    void ***REMOVED***SendRoomBookingConfirmationEmail_RoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            emailService.sendRoomBookingConfirmationEmail(roomBookingDTO);
        } catch (Exception e) {
            assert(e instanceof AppEx);
            assert(((AppEx) e).getErrorCode() == ErrorCode.ROOM_NOT_FOUND);
        }
    }

    @Test
    void ***REMOVED***SendRoomBookingConfirmationEmail_UserNotFound() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        try {
            emailService.sendRoomBookingConfirmationEmail(roomBookingDTO);
        } catch (Exception e) {
            assert(e instanceof AppEx);
            assert(((AppEx) e).getErrorCode() == ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    void ***REMOVED***SendMeetingReminderEmail() throws MessagingException {
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String to = "***REMOVED***user@example.com";
        String userName = "Test User";
        String roomName = "Room A";
        String startTime = "2025-03-28T10:00:00";
        String endTime = "2025-03-28T12:00:00";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendMeetingReminderEmail(to, userName, roomName, startTime, endTime);

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
