package com.scape.ReservationSchedule;

import com.scape.room.RoomDAO;
import com.scape.room.RoomDTO;
import java.util.*;

public class ReservationScheduleService {
    private final ReservationScheduleDAO dao = new ReservationScheduleDAO();

    // 1. 매장별 예약 가능한 날짜 목록
    public List<String> getAvailableDatesByStore(String storeLocation) {
        return dao.selectDatesByStore(storeLocation);
    }

    // 2. 날짜 + 매장 기준 예약 가능한 방 및 시간 목록
    public Map<RoomDTO, List<ReservationScheduleDTO>> getAvailableSchedulesByStoreAndDate(String store, String date) {
        return dao.selectSchedulesByStoreAndDate(store, date);
    }

    // 3. roomId + 날짜 + 시간 기준 schedule_id 조회
    public int getScheduleId(String roomId, String date, String time) {
        return dao.selectScheduleId(roomId, date, time);
    }

    // 4. 실제 예약 처리 + 취소
    public boolean reserve(int scheduleId, String userId, int headcount) {
        return dao.reserve(scheduleId, userId, headcount);
    }
    
    public List<ReservationScheduleDTO> getMyReservations(String userId) {
        return dao.selectReservationsByUser(userId);
    }

    public boolean cancelReservation(int scheduleId) {
        return dao.cancelReservation(scheduleId);
    }

    public RoomDTO getRoomInfo(String roomId) {
        return new RoomDAO().findRoomById(roomId);
    }

}