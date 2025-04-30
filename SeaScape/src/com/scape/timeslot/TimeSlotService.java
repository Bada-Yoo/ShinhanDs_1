package com.scape.timeslot;

import java.util.List;

import com.scape.room.RoomDTO;

public class TimeSlotService {
    private final TimeSlotDAO dao = new TimeSlotDAO();

    public boolean insertRoomTimeSlot(String roomId, String time) {
        TimeSlotDTO slot = TimeSlotDTO.builder()
            .room_id(roomId)
            .reservation_time(time)
            .build();

        return dao.insertTimeSlot(slot);
    }

    public List<RoomDTO> getRoomsWithoutTimeSlot() {
        return dao.findRoomsWithoutTimeSlot();
    }
}