package com.scape.ReservationSchedule;

import com.scape.activate.DBUtil;
import com.scape.room.RoomDTO;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReservationScheduleDAO {

    // 1. 매장 위치별 예약 가능한 날짜 조회
    public List<String> selectDatesByStore(String storeLocation) {
        List<String> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT reservation_date " +
                     "FROM reservationschedule rs JOIN room r ON rs.room_id = r.room_id " +
                     "WHERE r.store_unique_id = (SELECT store_unique_id FROM store WHERE location = ?) " +
                     "AND rs.is_reserved = 'N' ORDER BY reservation_date";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, storeLocation);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                dates.add(rs.getDate("reservation_date").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    // 2. 날짜와 매장으로 예약 가능한 방과 시간 목록 조회
    public Map<RoomDTO, List<ReservationScheduleDTO>> selectSchedulesByStoreAndDate(String storeLocation, String date) {
        Map<RoomDTO, List<ReservationScheduleDTO>> map = new LinkedHashMap<>();
        String sql = "SELECT r.*, rs.schedule_id, rs.reservation_date, rs.reservation_time " +
                     "FROM reservationschedule rs JOIN room r ON rs.room_id = r.room_id " +
                     "WHERE r.store_unique_id = (SELECT store_unique_id FROM store WHERE location = ?) " +
                     "AND rs.reservation_date = TO_DATE(?, 'YYYY-MM-DD') AND rs.is_reserved = 'N' " +
                     "ORDER BY r.room_id, rs.reservation_time";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, storeLocation);
            pst.setString(2, date);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .IS_19(rs.getInt("is_19"))
                        .PRICE(rs.getInt("price"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .SYNOPSIS(rs.getString("synopsis"))
                        .build();

                String dateStr = rs.getString("reservation_date").split(" ")[0];
                ReservationScheduleDTO schedule = ReservationScheduleDTO.builder()
                        .SCHEDULE_ID(rs.getInt("schedule_id"))
                        .ROOM_ID(rs.getString("room_id"))
                        .RESERVATION_DATE(Date.valueOf(dateStr))
                        .RESERVATION_TIME(rs.getString("reservation_time"))
                        .build();

                map.computeIfAbsent(room, k -> new ArrayList<>()).add(schedule);
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    // 3. schedule_id 조회
    public int selectScheduleId(String roomId, String date, String time) {
        String sql = "SELECT schedule_id FROM reservationschedule " +
                     "WHERE room_id = ? AND reservation_date = TO_DATE(?, 'YYYY-MM-DD') AND reservation_time = ? AND is_reserved = 'N'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, roomId);
            pst.setString(2, date);
            pst.setString(3, time);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) return rs.getInt("schedule_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 4. 예약 처리
    public boolean reserve(int scheduleId, String userId, int headcount) {
        String sql = "UPDATE reservationschedule SET user_id = ?, headcount = ?, is_reserved = 'Y' WHERE schedule_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, userId);
            pst.setInt(2, headcount);
            pst.setInt(3, scheduleId);

            int result = pst.executeUpdate();
            conn.commit();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}