package org.training.meetingroombooking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Phương thức chung để gửi email HTML
    public void sendHtmlEmail(String to, String subject,
                              String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("***REMOVED***");

        mailSender.send(message);
    }

    public void sendRoomBookingConfirmationEmail(RoomBookingDTO dto) throws MessagingException {
        String subject = "Meeting room booking confirmation successful";
        String htmlBody = "<h3>Greet " + dto.getUserName() + ",</h3>"
                + "<p>You have successfully booked a meeting room!</p>"
                + "<ul>"
                + "<li><b>Room:</b> " + dto.getRoomName() + "</li>"
                + "<li><b>Time:</b> " + dto.getStartTime() + " - " + dto.getEndTime() + "</li>"
                + "<li><b>Status:</b> " + dto.getStatus() + "</li>"
                + "</ul>"
                + "<p>Thank you for using our service!</p>";
        sendHtmlEmail(dto.getUserEmail(), subject, htmlBody);
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
