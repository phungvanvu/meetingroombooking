package org.training.meetingroombooking.entity.dto.Summary;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class BookingSummaryDTO {
  private Integer period;
  private Long bookings;

  public BookingSummaryDTO(Integer period, Long bookings) {
    this.period   = period;
    this.bookings = bookings;
  }
}
