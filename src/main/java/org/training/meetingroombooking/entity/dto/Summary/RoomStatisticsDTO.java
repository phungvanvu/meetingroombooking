package org.training.meetingroombooking.entity.dto.Summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomStatisticsDTO {
    private long totalRooms;
    private long availableRooms;
    private long unavailableRooms;
}
