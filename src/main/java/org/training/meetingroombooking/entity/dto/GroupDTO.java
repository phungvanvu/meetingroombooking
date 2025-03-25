package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO {

    @NotBlank(message = "Group name cannot be left blank")
    @Size(max = 50, message = "Group name cannot exceed 50 characters")
    private String groupName;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Size(max = 50, message = "Division must not exceed 100 characters.")
    private String division;

    @Size(max = 50, message = "Department must not exceed 50 characters")
    private String department;

    private LocalDateTime createdDate;
}