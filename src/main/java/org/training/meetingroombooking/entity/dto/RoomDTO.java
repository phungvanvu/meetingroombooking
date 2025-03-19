package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

  @NotBlank(message = "Room name cannot be left blank")
  @Size(max = 100, message = "Room name cannot exceed 100 characters")
  private String roomName;

  @NotBlank(message = "Location cannot be left blank")
  @Size(max = 255, message = "Location must not exceed 255 characters")
  private String location;

  @NotNull(message = "Capacity cannot be null")
  @PositiveOrZero(message = "Capacity must be non-negative")
  private Integer capacity;

  private boolean available;

  @Size(max = 255, message = "Notes cannot exceed 255 characters")
  private String note;

  private boolean active;

  private String imageUrl;

  private List<String> equipments;
}

