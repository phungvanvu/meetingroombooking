package org.training.meetingroombooking.entity.dto.Summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingSummaryDTO {
  private int period;
  private int bookings;
}
