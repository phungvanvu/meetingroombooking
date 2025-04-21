package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

  private Long roomId;

  @NotBlank(message = "{room.name.notblank}")
  @Size(max = 100, message = "{room.name.maxsize}")
  private String roomName;

  @NotBlank(message = "{room.location.notblank}")
  @Size(max = 255, message = "{room.location.maxsize}")
  private String location;

  @NotNull(message = "{room.capacity.notnull}")
  @PositiveOrZero(message = "{room.capacity.positive}")
  private Integer capacity;

  @NotNull(message = "{room.available.notnull}")
  private boolean available;

  @Size(max = 255, message = "{room.note.maxsize}")
  private String note;

  private List<String> imageUrls;

  private Set<String> equipments;
}
