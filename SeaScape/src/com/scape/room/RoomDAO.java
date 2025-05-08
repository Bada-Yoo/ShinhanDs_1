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

    // SQL 쿼리 모음
    private static final String INSERT_ROOM = """
        INSERT INTO room (creator_id, room_name, genre, is_19, price, limit_time, synopsis)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String FIND_ROOM_BY_CREATOR = "SELECT * FROM room WHERE creator_id = ?";
    private static final String UPDATE_ROOM_REQUEST = "UPDATE room SET hope_store = ?, store_status = '대기중' WHERE room_id = ? AND creator_id = ?";
    private static final String DELETE_TIMESLOT = "DELETE FROM timeslot WHERE room_id = ?";
    private static final String DELETE_ROOM = "DELETE FROM room WHERE room_id = ? AND creator_id = ?";
    private static final String FIND_UNASSIGNED_ROOMS = """
        SELECT room_id, room_name, genre, synopsis, store_status, hope_store
        FROM room
        WHERE creator_id = ? AND store_unique_id IS NULL
    """;
    private static final String FIND_ROOMS_BY_STATUS = "SELECT * FROM room WHERE store_status = ?";
    private static final String COUNT_ROOMS_IN_STORE = """
        SELECT COUNT(*) FROM room
        WHERE store_unique_id IN (
            SELECT store_unique_id FROM store WHERE location = ?
        )
    """;
    private static final String UPDATE_STORE_STATUS = "UPDATE room SET store_status = ? WHERE room_id = ?";
    private static final String ASSIGN_STORE = """
        UPDATE room
        SET store_unique_id = ?, store_status = NULL, hope_store = NULL
        WHERE room_id = ?
    """;
    private static final String FIND_ROOM_BY_ID = "SELECT * FROM room WHERE room_id = ?";
    private static final String FIND_ROOMS_NO_TIMESLOT_ASSIGNED_STORE = """
        SELECT * FROM room
        WHERE store_unique_id IS NOT NULL
        AND room_id NOT IN (SELECT room_id FROM timeslot)
    """;
    
    // 방 생성
    public boolean insertRoom(RoomDTO room) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(FIND_ROOMS_NO_TIMESLOT_ASSIGNED_STORE);
            pst.setString(1, room.getCREATOR_ID());
            pst.setString(2, room.getROOM_NAME());
            pst.setString(3, room.getGENRE());
            pst.setInt(4, room.getIS_19());
            pst.setInt(5, room.getPRICE());
            pst.setInt(6, room.getLIMIT_TIME());
            pst.setString(7, room.getSYNOPSIS());

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

    // 방 삭제
    public boolean deleteRoomByCreator(String roomId, String creatorId) {
        boolean result = false;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
            pst1 = conn.prepareStatement(DELETE_TIMESLOT);
            pst1.setString(1, roomId);
            pst1.executeUpdate();

            pst2 = conn.prepareStatement(DELETE_ROOM);
            pst2.setString(1, roomId);
            pst2.setString(2, creatorId);

            int count = pst2.executeUpdate();
            if (count > 0) {
                conn.commit();
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst2, null);
            DBUtil.dbDisconnect(null, pst1, null);
        }

        return result;
    }

    // 방 목록 조회
    public List<RoomDTO> findRoomsByCreator(String creatorId) {
        List<RoomDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(FIND_ROOM_BY_CREATOR);
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

    // 안 배정된 방 보기
    public List<RoomDTO> findUnassignedRooms(String creatorId) {
        List<RoomDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();

        try (PreparedStatement pst = conn.prepareStatement(FIND_UNASSIGNED_ROOMS)) {
            pst.setString(1, creatorId);
            rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .HOPE_STORE(rs.getString("hope_store"))
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .SYNOPSIS(rs.getString("synopsis"))
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

    // 배정 요청 업데이트
    public boolean updateRoomRequest(String roomId, String creatorId, String hopeStore) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_ROOM_REQUEST);
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

    // 대기중인 store 찾기
    public List<RoomDTO> findRoomsByStatus(String status) {
        List<RoomDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(FIND_ROOMS_BY_STATUS);
            pst.setString(1, status);
            rs = pst.executeQuery();

            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .SYNOPSIS(rs.getString("synopsis"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .PRICE(rs.getInt("price"))
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

    // 가게 사이즈 찾기
    public int countRoomsInStore(String hopeStore) {
        int count = 0;
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(COUNT_ROOMS_IN_STORE);
            pst.setString(1, hopeStore);
            rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return count;
    }

    // 대기 상태 바꾸기
    public void updateStoreStatus(String roomId, String status) {
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(UPDATE_STORE_STATUS);
            pst.setString(1, status);
            pst.setString(2, roomId);
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
    }

    // 방 배정해주기
    public void assignStoreToRoom(String roomId, String storeUniqueId) {
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(ASSIGN_STORE);
            pst.setString(1, storeUniqueId);
            pst.setString(2, roomId);
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
    }

    // 방 찾기
    public RoomDTO findRoomById(String roomId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(FIND_ROOM_BY_ID)) {

            pst.setString(1, roomId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .GENRE(rs.getString("genre"))
                        .IS_19(rs.getInt("is_19"))
                        .PRICE(rs.getInt("price"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .SYNOPSIS(rs.getString("synopsis"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 예약방 찾기
    public List<RoomDTO> findRoomsWithoutTimeSlotButAssignedToStore() {
        List<RoomDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_ROOM);
            rs = pst.executeQuery();
            while (rs.next()) {
                RoomDTO room = RoomDTO.builder()
                        .ROOM_ID(rs.getString("room_id"))
                        .ROOM_NAME(rs.getString("room_name"))
                        .LIMIT_TIME(rs.getInt("limit_time"))
                        .STORE_UNIQUE_ID(rs.getString("store_unique_id"))
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

}
