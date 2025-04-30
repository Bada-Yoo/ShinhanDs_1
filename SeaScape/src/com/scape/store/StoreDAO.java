package com.scape.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scape.activate.DBUtil;

public class StoreDAO {
	
	Connection conn;
    PreparedStatement pst;
    ResultSet rs;
	//매점 위치 따기
	public List<String> getAllLocations() {
	    List<String> locations = new ArrayList<>();
	    conn = DBUtil.getConnection();
	    pst = null;
	    rs = null;

	    try {
	        String sql = "SELECT DISTINCT location FROM store";
	        pst = conn.prepareStatement(sql);
	        rs = pst.executeQuery();

	        while (rs.next()) {
	            locations.add(rs.getString("location"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.dbDisconnect(conn, pst, rs);
	    }

	    return locations;
	}
	//위치 사이즈찾기
	public int findSizeLimitByLocation(String location) {
        int limit = 0;
        String sql = "SELECT size_limit FROM store WHERE location = ?";
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, location);
            rs = pst.executeQuery();

            if (rs.next()) {
                limit = rs.getInt("size_limit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return limit;
    }
	
	//매장고유id찾기
	public String findStoreUniqueIdByLocation(String location) {
	    String id = null;
	    String sql = "SELECT store_unique_id FROM store WHERE location = ?";
	    conn = DBUtil.getConnection();
	    try {
	        pst = conn.prepareStatement(sql);
	        pst.setString(1, location);
	        rs = pst.executeQuery();
	        if (rs.next()) {
	            id = rs.getString("store_unique_id");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.dbDisconnect(conn, pst, rs);
	    }
	    return id;
	}
	
	//매장위치얻기
	public String getLocationByRoomId(String roomId) {
	    String sql = "SELECT s.location FROM store s JOIN room r ON s.store_unique_id = r.store_unique_id WHERE r.room_id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pst = conn.prepareStatement(sql)) {

	        pst.setString(1, roomId);
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            return rs.getString("location");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "알 수 없음";
	}
	
	//매장 로그인
	public boolean checkStoreLogin(String id, String pw) {
	    String sql = "SELECT * FROM store WHERE store_id = ? AND store_pw = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pst = conn.prepareStatement(sql)) {

	        pst.setString(1, id);
	        pst.setString(2, pw);
	        ResultSet rs = pst.executeQuery();

	        return rs.next(); // 로그인 성공 조건: 레코드 존재

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}



}
