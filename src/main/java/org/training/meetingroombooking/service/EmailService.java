package org.training.meetingroombooking.service;

import jakarta.mail.MessagingException;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;

public interface EmailService {

  void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;

  void sendRoomBookingConfirmationEmail(RoomBookingDTO dto);

  void sendMeetingReminderEmail(
      String to, String userName, String roomName, String startTime, String endTime)
      throws MessagingException;
}
