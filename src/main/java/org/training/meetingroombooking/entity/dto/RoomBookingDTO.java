package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.Purpose;
import org.training.meetingroombooking.validation.StartBeforeEnd;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StartBeforeEnd
public class RoomBookingDTO {

  private Long bookingId;

  @NotNull(message = "{roomBooking.roomId.notnull}")
  private Long roomId;

  private String roomName;

  private String userEmail;

  @NotNull(message = "{roomBooking.bookedById.notnull}")
  private Long bookedById;

  private String userName;

  @FutureOrPresent(message = "{roomBooking.startTime.futureOrPresent}")
  @NotNull(message = "{roomBooking.startTime.notnull}")
  private LocalDateTime startTime;

  @FutureOrPresent(message = "{roomBooking.endTime.futureOrPresent}")
  @NotNull(message = "{roomBooking.endTime.notnull}")
  private LocalDateTime endTime;

  @NotNull(message = "{roomBooking.purpose.notnull}")
  private Purpose purpose;

  private BookingStatus status;

  @Size(max = 255, message = "{roomBooking.description.maxsize}")
  private String description;

  private LocalDateTime createdAt;
}
