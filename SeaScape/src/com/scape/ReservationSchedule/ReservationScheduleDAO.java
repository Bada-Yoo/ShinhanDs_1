package com.scape.ReservationSchedule;

import com.scape.activate.DBUtil;
import com.scape.room.RoomDTO;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ReservationScheduleDAO {

    // SQL 상수 정리
    private static final String SELECT_DATES_BY_STORE = """
        SELECT DISTINCT reservation_date
        FROM reservationschedule rs
        JOIN room r ON rs.room_id = r.room_id
        WHERE r.store_unique_id = (SELECT store_unique_id FROM store WHERE location = ?)
        AND rs.is_reserved = 'N'
        ORDER BY reservation_date
    """;

    private static final String SELECT_SCHEDULES_BY_STORE_AND_DATE = """
        SELECT r.*, rs.schedule_id, rs.reservation_date, rs.reservation_time
        FROM reservationschedule rs
        JOIN room r ON rs.room_id = r.room_id
        WHERE r.store_unique_id = (SELECT store_unique_id FROM store WHERE location = ?)
        AND rs.reservation_date = TO_DATE(?, 'YYYY-MM-DD')
        AND rs.is_reserved = 'N'
        ORDER BY r.room_id, rs.reservation_time
    """;

    private static final String SELECT_SCHEDULE_ID = """
        SELECT schedule_id FROM reservationschedule
        WHERE room_id = ?
        AND reservation_date = TO_DATE(?, 'YYYY-MM-DD')
        AND reservation_time = ?
        AND is_reserved = 'N'
    """;

    private static final String RESERVE_SCHEDULE = """
        UPDATE reservationschedule
        SET user_id = ?, headcount = ?, is_reserved = 'Y'
        WHERE schedule_id = ?
    """;

    private static final String SELECT_RESERVATIONS_BY_USER = """
        SELECT * FROM reservationschedule
        WHERE user_id = ? AND is_reserved = 'Y'
    """;

    private static final String CANCEL_RESERVATION = """
        UPDATE reservationschedule
        SET user_id = NULL, headcount = 0, is_reserved = 'N'
        WHERE schedule_id = ?
    """;

    private static final String SELECT_RESERVATIONS_BY_STORE_AND_DATE = """
        SELECT rs.* FROM reservationschedule rs
        JOIN room r ON rs.room_id = r.room_id
        JOIN store s ON r.store_unique_id = s.store_unique_id
        WHERE s.store_id = ? AND rs.reservation_date BETWEEN ? AND ?
        ORDER BY rs.reservation_date, rs.reservation_time
    """;

    private static final String DELETE_RESERVATIONS_BY_DATE_AND_STORE = """
        DELETE FROM reservationschedule
        WHERE room_id IN (
            SELECT r.room_id FROM room r
            JOIN store s ON r.store_unique_id = s.store_unique_id
            WHERE s.store_id = ?
        ) AND reservation_date = ?
    """;

    // 1. 매장 위치별 예약 가능한 날짜 조회
    public List<String> selectDatesByStore(String storeLocation) {
        List<String> dates = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_DATES_BY_STORE)) {

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
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_SCHEDULES_BY_STORE_AND_DATE)) {

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
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_SCHEDULE_ID)) {

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
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(RESERVE_SCHEDULE)) {

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

    // 예약 조회 (사용자 기준)
    public List<ReservationScheduleDTO> selectReservationsByUser(String userId) {
        List<ReservationScheduleDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_RESERVATIONS_BY_USER)) {

            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ReservationScheduleDTO dto = ReservationScheduleDTO.builder()
                    .SCHEDULE_ID(rs.getInt("schedule_id"))
                    .ROOM_ID(rs.getString("room_id"))
                    .USER_ID(rs.getString("user_id"))
                    .RESERVATION_DATE(rs.getDate("reservation_date"))
                    .RESERVATION_TIME(rs.getString("reservation_time"))
                    .HEADCOUNT(rs.getInt("headcount"))
                    .IS_RESERVED(rs.getString("is_reserved"))
                    .build();

                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 예약 취소
    public boolean cancelReservation(int scheduleId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(CANCEL_RESERVATION)) {

            pst.setInt(1, scheduleId);
            int result = pst.executeUpdate();
            conn.commit();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 예약 조회 (매장 기준)
    public List<ReservationScheduleDTO> selectReservationsByStoreAndDate(String storeId, LocalDate start, LocalDate end) {
        List<ReservationScheduleDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_RESERVATIONS_BY_STORE_AND_DATE)) {

            pst.setString(1, storeId);
            pst.setDate(2, Date.valueOf(start));
            pst.setDate(3, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ReservationScheduleDTO dto = ReservationScheduleDTO.builder()
                    .SCHEDULE_ID(rs.getInt("schedule_id"))
                    .ROOM_ID(rs.getString("room_id"))
                    .USER_ID(rs.getString("user_id"))
                    .RESERVATION_DATE(rs.getDate("reservation_date"))
                    .RESERVATION_TIME(rs.getString("reservation_time"))
                    .HEADCOUNT(rs.getInt("headcount"))
                    .IS_RESERVED(rs.getString("is_reserved"))
                    .build();
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 당일 예약창 삭제
    public boolean deleteReservationsByDateAndStore(String storeId, LocalDate date) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(DELETE_RESERVATIONS_BY_DATE_AND_STORE)) {

            pst.setString(1, storeId);
            pst.setDate(2, Date.valueOf(date));
            int result = pst.executeUpdate();
            conn.commit();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
