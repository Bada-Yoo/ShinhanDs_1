package com.scape.room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scape.activate.DBUtil;

public class RoomDAO {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    // ✅ 간결한 SQL 상수명
    private static final String INSERT_ROOM =
    	    "INSERT INTO room (creator_id, room_name, genre, is_19, price, limit_time, synopsis, store_unique_id, hope_store, store_status) " +
    	    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    private static final String DELETE_ROOM =
        "DELETE FROM room WHERE room_id = ? AND creator_id = ?";

    private static final String FIND_ROOM =
        "SELECT * FROM room WHERE creator_id = ?";

    private static final String UPDATE_ROOM =
        "UPDATE room SET hope_store = ?, store_status = '대기중' WHERE room_id = ? AND creator_id = ?";

    // ✅ 방 생성
    public boolean insertRoom(RoomDTO room) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_ROOM);
            pst.setString(1, room.getROOM_ID());
            pst.setString(2, room.getCREATOR_ID());
            pst.setString(3, room.getROOM_NAME());
            pst.setString(4, room.getGENRE());
            pst.setInt(5, room.getIS_19());
            pst.setInt(6, room.getPRICE());
            pst.setInt(7, room.getLIMIT_TIME());
            pst.setString(8, room.getSYNOPSIS());

            int count = pst.executeUpdate();
            if (count > 0) {
                conn.commit();
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }

    // ✅ 방 삭제
    public boolean deleteRoomByCreator(String roomId, String creatorId) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE_ROOM);
            pst.setString(1, roomId);
            pst.setString(2, creatorId);

            int count = pst.executeUpdate();
            if (count > 0) {
                conn.commit();
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }

    // ✅ 방 목록 조회
    public List<RoomDTO> findRoomsByCreator(String creatorId) {
        List<RoomDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(FIND_ROOM);
            pst.setString(1, creatorId);
            rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .CREATOR_ID(rs.getString("creator_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .IS_19(rs.getInt("is_19"))
                        .PRICE(rs.getInt("price"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .SYNOPSIS(rs.getString("synopsis"))
                        .STORE_UNIQUE_ID(rs.getString("store_unique_id"))
                        .HOPE_STORE(rs.getString("hope_store"))
                        .STORE_STATUS(rs.getString("store_status"))
                        .build();

                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return list;
    }
    
    //안배정된 방보기
    public List<RoomDTO> findUnassignedRooms(String creatorId) {
        List<RoomDTO> list = new ArrayList<>();
        String sql = "SELECT room_name, genre, synopsis FROM room WHERE creator_id = ? AND store_unique_id IS NULL";
        conn = DBUtil.getConnection();

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, creatorId);
            rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .SYNOPSIS(rs.getString("synopsis"))
                        .build();
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return list;
    }

    // ✅ 배정 요청 업데이트
    public boolean updateRoomRequest(String roomId, String creatorId, String hopeStore) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_ROOM);
            pst.setString(1, hopeStore);
            pst.setString(2, roomId);
            pst.setString(3, creatorId);

            int count = pst.executeUpdate();
            if (count > 0) {
                conn.commit();
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }
}
