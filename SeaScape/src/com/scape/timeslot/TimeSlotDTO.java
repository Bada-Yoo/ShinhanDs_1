package com.scape.timeslot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    private String room_id;
    private String reservation_time;
}
