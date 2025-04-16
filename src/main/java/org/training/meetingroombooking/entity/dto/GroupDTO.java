package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO {

  @NotBlank(message = "{group.notblank}")
  @Size(max = 50, message = "{group.name.maxsize}")
  private String groupName;

  @Size(max = 100, message = "{group.location.maxsize}")
  private String location;

  @Size(max = 50, message = "{group.division.maxsize}")
  private String division;

  private LocalDateTime createdDate;
}
