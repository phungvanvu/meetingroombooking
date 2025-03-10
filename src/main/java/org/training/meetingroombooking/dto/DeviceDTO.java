package org.training.meetingroombooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.training.meetingroombooking.entity.Room;

public class DeviceDTO {
  @NotNull
  @Size(min = 3, max = 50, message = "deviceName must be between 3 and 50 characters")
  private String deviceName;

  private Room room;
}
