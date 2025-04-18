package org.training.meetingroombooking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.repository.UserRepository;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender mailSender, RoomRepository roomRepository,
                        UserRepository userRepository) {
        this.mailSender = mailSender;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // Phương thức chung để gửi email HTML
    @Async
    public void sendHtmlEmail(String to, String subject,
                              String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("phungvanvu0@gmail.com");
        mailSender.send(message);
    }

    @Async
    public void sendRoomBookingConfirmationEmail(RoomBookingDTO dto) {
        try {
            Room room = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
            User user = userRepository.findById(dto.getBookedById())
                    .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));

            String userEmail = user.getEmail();
            System.out.println("Sending email to: " + userEmail);

            String subject = "Meeting room booking confirmation successful";
            String htmlBody = "<h3>Greet " + (user.getFullName() != null ? user.getFullName() : "N/A") + ",</h3>"
                    + "<p>You have successfully booked a meeting room!</p>"
                    + "<ul>"
                    + "<li><b>Title:</b> " + (dto.getPurpose() != null ? dto.getPurpose().toString() : "N/A") + "</li>"
                    + "<li><b>Room:</b> " + (room.getRoomName() != null ? room.getRoomName() : "N/A") + "</li>"
                    + "<li><b>Time:</b> " + (dto.getStartTime() != null ? dto.getStartTime().toString() : "N/A")
                    + " - " + (dto.getEndTime() != null ? dto.getEndTime().toString() : "N/A") + "</li>"
                    + "<li><b>Status:</b> " + (dto.getStatus() != null ? dto.getStatus().toString() : "N/A") + "</li>"
                    + "<li><b>Description:</b> " + (dto.getDescription() != null ? dto.getDescription() : "N/A") + "</li>"
                    + "<li><b>Created at:</b> " + (dto.getCreatedAt() != null ? dto.getCreatedAt().toString() : "N/A") + "</li>"
                    + "</ul>"
                    + "<p>Thank you for using our service!</p>";
            sendHtmlEmail(userEmail, subject, htmlBody);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendMeetingReminderEmail(String to, String userName,
                                         String roomName, String startTime,
                                         String endTime) throws MessagingException {
        String subject = "Upcoming meeting reminder";
        String htmlBody = "<h3>Greet " + userName + ",</h3>"
                + "<p>You have a meeting coming up.</p>"
                + "<ul>"
                + "<li><b>Room:</b> " + roomName + "</li>"
                + "<li><b>Time:</b> " + startTime + " - " + endTime + "</li>"
                + "</ul>"
                + "<p>Please arrive on time!</p>";
        sendHtmlEmail(to, subject, htmlBody);
    }

}
