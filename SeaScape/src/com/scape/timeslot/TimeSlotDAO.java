package com.scape.timeslot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scape.activate.DBUtil;
import com.scape.room.RoomDTO;

public class TimeSlotDAO {

    public boolean insertTimeSlot(TimeSlotDTO slot) {
        String sql = "INSERT INTO TimeSlot (room_id, reservation_time) VALUES (?, ?)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, slot.getRoom_id());
            pst.setString(2, slot.getReservation_time());

            int result = pst.executeUpdate();
            conn.commit();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
    }

    public List<RoomDTO> findRoomsWithoutTimeSlot() {
        List<RoomDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM room r WHERE NOT EXISTS (" +
                     "SELECT 1 FROM timeslot t WHERE r.room_id = t.room_id)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO dto = RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return list;
    }
}