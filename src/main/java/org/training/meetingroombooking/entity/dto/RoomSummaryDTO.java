package org.training.meetingroombooking.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomSummaryDTO {
    private Long roomId;
    private String roomName;
    private long bookingCount;
}
