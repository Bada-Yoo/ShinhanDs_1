package com.scape.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scape.activate.DBUtil;

public class StoreDAO {
	
	public List<String> getAllLocations() {
	    List<String> locations = new ArrayList<>();
	    Connection conn = DBUtil.getConnection();
	    PreparedStatement pst = null;
	    ResultSet rs = null;

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
}
