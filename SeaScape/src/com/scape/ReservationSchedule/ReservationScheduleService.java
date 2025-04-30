package com.scape.ReservationSchedule;

import com.scape.room.RoomDAO;
import com.scape.room.RoomDTO;
import com.scape.users.UsersDAO;

import java.time.LocalDate;
import java.util.*;

public class ReservationScheduleService {
    private final ReservationScheduleDAO dao = new ReservationScheduleDAO();

    // 매장별 예약 가능한 날짜 목록
    public List<String> getAvailableDatesByStore(String storeLocation) {
        return dao.selectDatesByStore(storeLocation);
    }

    // 날짜 + 매장 기준 예약 가능한 방 및 시간 목록
    public Map<RoomDTO, List<ReservationScheduleDTO>> getAvailableSchedulesByStoreAndDate(String store, String date) {
        return dao.selectSchedulesByStoreAndDate(store, date);
    }

    // roomId + 날짜 + 시간 기준 schedule_id 조회
    public int getScheduleId(String roomId, String date, String time) {
        return dao.selectScheduleId(roomId, date, time);
    }

    // 실제 예약 처리 + 취소
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
    
    // 매장 ID로 예약 전체 조회 (날짜 범위 포함)
    public List<ReservationScheduleDTO> getReservationsByStoreAndDate(String storeId, LocalDate start, LocalDate end) {
        return dao.selectReservationsByStoreAndDate(storeId, start, end);
    }

    //  특정 날짜의 해당 매장 예약 모두 삭제
    public boolean deleteReservationsByDateAndStore(String storeId, LocalDate date) {
        return dao.deleteReservationsByDateAndStore(storeId, date);
    }
    //이름찾기
    public String getUserName(String userId) {
        return new UsersDAO().findUserNameById(userId);
    }



}