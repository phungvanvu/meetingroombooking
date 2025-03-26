package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.Purpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBookingDTO {

    private Long bookingId;

    @NotNull(message = "Room cannot be null")
    private Long roomId;

    private String roomName;

    private String userEmail;

    @NotNull(message = "The person placing the order cannot be left blank.")
    private Long bookedById;

    private String userName;

//    @NotNull(message = "Request cannot be Null")
//    private Long requestId;

    @FutureOrPresent(message = "The start time must be present or in the future.")
    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @FutureOrPresent(message = "The end time must be present or in the future.")
    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;

    @NotNull(message = "Purpose cannot be null")
    private Purpose purpose;

    @NotNull(message = "Status cannot be null")
    private BookingStatus status;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private LocalDateTime createdAt;
}

