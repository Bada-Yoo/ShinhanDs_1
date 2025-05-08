package com.scape.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.scape.activate.DBUtil;

public class UsersDAO {
    Connection conn;
    PreparedStatement pst;

    // SQL 상수 정의
    private static final String INSERT_USER = "INSERT INTO users (user_id, user_pw, user_name, user_nickname, user_age, escape_history) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String LOGIN_USER = "SELECT * FROM users WHERE user_id = ? AND user_pw = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_USER_AGE = "SELECT user_age FROM users WHERE user_id = ?";
    private static final String FIND_USER_NAME = "SELECT user_name FROM users WHERE user_id = ?";

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
            result = pst.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("unique constraint") || e.getMessage().contains("ORA-00001")) {
                result = -1; // 아이디 중복 에러
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

    // 회원 삭제
    public int deleteUser(String userId) {
        int result = 0;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE_USER);
            pst.setString(1, userId);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return result;
    }

    // 나이 찾기
    public int findAgeById(String userId) {
        int age = 0;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(FIND_USER_AGE)) {

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

    // 이름 찾기
    public String findUserNameById(String userId) {
        String name = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(FIND_USER_NAME)) {

            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                name = rs.getString("user_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name != null ? name : "(알 수 없음)";
    }
} 
