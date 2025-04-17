package org.training.meetingroombooking.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.repository.UserRepository;
import org.training.meetingroombooking.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final TemplateEngine templateEngine;

  public EmailServiceImpl(
      JavaMailSender mailSender,
      RoomRepository roomRepository,
      UserRepository userRepository,
      TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
    this.templateEngine = templateEngine;
  }

  @Async
  @Override
  public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);
    helper.setFrom("phungvanvu0@gmail.com");
    mailSender.send(message);
  }

  @Async
  @Override
  public void sendRoomBookingConfirmationEmail(RoomBookingDTO dto) {
    try {
      Room room =
          roomRepository
              .findById(dto.getRoomId())
              .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
      User user =
          userRepository
              .findById(dto.getBookedById())
              .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      Context ctx = new Context();
      ctx.setVariable("userName", user.getFullName());
      ctx.setVariable("roomName", room.getRoomName());
      ctx.setVariable("purpose", dto.getPurpose());
      ctx.setVariable("status", dto.getStatus());
      ctx.setVariable("description", dto.getDescription());
      ctx.setVariable("startTime", dto.getStartTime().format(fmt));
      ctx.setVariable("endTime", dto.getEndTime().format(fmt));
      ctx.setVariable("createdAt", dto.getCreatedAt().format(fmt));
      String htmlContent = templateEngine.process("booking-confirmation", ctx);
      sendHtmlEmail(user.getEmail(), "Meeting room booking confirmation", htmlContent);
    } catch (MessagingException e) {
      System.err.println("Failed to send email: " + e.getMessage());
    }
  }

  @Async
  @Override
  public void sendMeetingReminderEmail(
      String to, String userName, String roomName, String startTime, String endTime)
      throws MessagingException {
    try {
      Context context = new Context();
      context.setVariable("userName", userName);
      context.setVariable("roomName", roomName);
      context.setVariable("startTime", startTime);
      context.setVariable("endTime", endTime);
      String htmlContent = templateEngine.process("meeting-reminder", context);
      sendHtmlEmail(to, "Upcoming Meeting Reminder", htmlContent);
    } catch (MessagingException e) {
      System.err.println("Failed to send email: " + e.getMessage());
    }
  }
}
