package com.scape.creator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.scape.activate.DBUtil;

public class CreatorDAO {
    Connection conn;
    PreparedStatement pst;
    static final String INSERT_CREATOR = "INSERT INTO creator (creator_id, creator_pw, creator_nickname) VALUES (?, ?, ?)";
    static final String LOGIN_CREATOR = "SELECT * FROM creator WHERE creator_id = ? AND creator_pw = ?";
    static final String FIND_CREATOR = "SELECT * FROM creator WHERE creator_id = ?";
    
    ResultSet rs;

    public boolean insertCreator(CreatorDTO creator) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            pst= conn.prepareStatement(INSERT_CREATOR);
            pst.setString(1, creator.getCREATOR_ID());
            pst.setString(2, creator.getCREATOR_PW());
            pst.setString(3, creator.getCREATOR_NICKNAME());
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

    public boolean checkLogin(String id, String pw) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
        	pst= conn.prepareStatement(LOGIN_CREATOR);
            pst.setString(1, id);
            pst.setString(2, pw);
            rs = pst.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return result;
    }
    
    public CreatorDTO findCreatorById(String id) {
        CreatorDTO creator = null;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(FIND_CREATOR);
            pst.setString(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                creator = CreatorDTO.builder()
                        .CREATOR_ID(rs.getString("creator_id"))
                        .CREATOR_PW(rs.getString("creator_pw"))
                        .CREATOR_NICKNAME(rs.getString("creator_nickname"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return creator;
    }

}
