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

}
