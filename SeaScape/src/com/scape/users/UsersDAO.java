package com.scape.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.scape.activate.DBUtil;

public class UsersDAO {
    Connection conn;
    PreparedStatement pst;
    static final String INSERT_USER = "INSERT INTO users (user_id, user_pw, user_name, user_nickname, user_age, escape_history) VALUES (?, ?, ?, ?, ?, ?)";
    static final String LOGIN_USER = "SELECT * FROM users WHERE user_id = ? AND user_pw = ?";

    // 회원가입
    public int insertUser(UsersDTO user) {
        int result = 0;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_USER);
            pst.setString(1, user.getUSER_ID());
            pst.setString(2, user.getUSER_PW());
            pst.setString(3, user.getUSER_NAME());
            pst.setString(4, user.getUSER_NICKNAME());
            pst.setInt(5, user.getUSER_AGE());
            pst.setInt(6, user.getESCAPE_HISTORY());

            result = pst.executeUpdate(); // 성공 시 1 반환

        } catch (SQLException e) {
            if (e.getMessage().contains("unique constraint") || e.getMessage().contains("ORA-00001")) {
                // 아이디 중복 에러
                result = -1;
            } else {
                e.printStackTrace();
            }
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }

    // 로그인
    public UsersDTO findUserByIdAndPw(String id, String pw) {
        UsersDTO user = null;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(LOGIN_USER);
            pst.setString(1, id);
            pst.setString(2, pw);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = UsersDTO.builder()
                        .USER_ID(rs.getString("user_id"))
                        .USER_PW(rs.getString("user_pw"))
                        .USER_NAME(rs.getString("user_name"))
                        .USER_NICKNAME(rs.getString("user_nickname"))
                        .USER_AGE(rs.getInt("user_age"))
                        .ESCAPE_HISTORY(rs.getInt("escape_history"))
                        .build();
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return user;
    }
    
    //회원삭제
    public int deleteUser(String userId) {
        int result = 0;
        conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM users WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }
    
    //나이찾기
    public int findAgeById(String userId) {
        int age = 0;
        String sql = "SELECT user_age FROM users WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                age = rs.getInt("user_age");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return age;
    }


}
