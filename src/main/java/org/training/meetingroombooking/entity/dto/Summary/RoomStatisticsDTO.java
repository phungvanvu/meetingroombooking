package org.training.meetingroombooking.entity.dto.Summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomStatisticsDTO {
    private long totalRooms;
    private long availableRooms;
    private long unavailableRooms;
    private long totalBookings;
    private long todayBookings;

    private List<RoomDTO> availableRoomList;
    private List<RoomDTO> unavailableRoomList;
    private List<RoomBookingDTO> todayBookingList;
}
