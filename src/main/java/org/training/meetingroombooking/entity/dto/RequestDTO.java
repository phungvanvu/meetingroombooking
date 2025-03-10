package org.training.meetingroombooking.entity.dto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.RequestStatus;
import org.training.meetingroombooking.entity.models.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {
    @Size(min = 1, max = 100, message = "title cannot be empty")
    private String title;

    @Size(min = 1, max = 100)
    private String location;

    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @Size(min = 1, max = 255)
    private String jobLevel;

    private RequestStatus status;

    @Size(min = 1, max = 100)
    private String approval;

    private LocalDate target;
    private LocalDate onboard;
    private User createdBy;
    private User hrPic;

    @Size(min = 1, max = 255)
    private String action;
}
