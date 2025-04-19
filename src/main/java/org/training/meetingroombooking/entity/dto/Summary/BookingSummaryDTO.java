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
  private Integer period;
  private Long bookings;

  public BookingSummaryDTO(Number period, Long bookings) {
    this.period = period == null ? null : period.intValue();
    this.bookings = bookings;
  }
}
