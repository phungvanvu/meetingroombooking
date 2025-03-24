package org.training.meetingroombooking.entity.dto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.RequestStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {

    @NotBlank(message = "Title cannot be left blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Location cannot left blank")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Size(max = 50, message = "jobLevel must not exceed 50 characters.")
    private String jobLevel;

    @NotNull(message = "Status cannot be null")
    private RequestStatus status;

    @Size(max = 50, message = "Approval cannot exceed 500 characters")
    private String approval;

    @FutureOrPresent(message = "Target date must be present or in the future.")
    private LocalDateTime target;

    @FutureOrPresent(message = "Onboard date must be present or in the future.")
    private LocalDateTime onboard;

    @NotNull(message = "Creator cannot be null")
    private Long createdBy;

    private Long hrPic;

    @Size(max = 75, message = "Action cannot exceed 75 characters")
    private String action;
}