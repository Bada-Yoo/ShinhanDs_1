package com.scape.creator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.scape.activate.DBUtil;

public class CreatorDAO {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public boolean insertCreator(CreatorDTO creator) {
        boolean result = false;
        conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO creator (creator_id, creator_pw, creator_nickname) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(sql);
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
            String sql = "SELECT * FROM creator WHERE creator_id = ? AND creator_pw = ?";
            pst = conn.prepareStatement(sql);
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
}
