package org.training.meetingroombooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoomDTO {
  @Size(min = 3, max = 50, message = "roomName must be between 3 and 50 characters")
  private String roomName;
  @NotNull
  private int roomCapacity;
  @NotNull
  @Size(min = 3, max = 255, message = "location must be between 3 and 255 characters")
  private String location;

  private boolean status;

  private String image;
}
