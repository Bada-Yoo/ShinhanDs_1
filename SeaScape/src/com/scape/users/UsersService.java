package com.scape.users;

import lombok.extern.java.Log;

@Log
public class UsersService {
    UsersDAO usersDao = new UsersDAO();

    // 회원가입
    public String insertId(UsersDTO users) {
        int result = usersDao.insertUser(users);

        if (result == 1) {
            return users.getUSER_NICKNAME();
        } else if (result == -1) {
            return "이미 존재하는 아이디입니다.";
        } else {
            return null; // 일반 실패
        }
    }

    // 로그인
    public UsersDTO login(String id, String pw) {
        return usersDao.findUserByIdAndPw(id, pw);
    }

    // 예약하기
    public String makeReservation() {
        // 예약 관련 로직
        return "예약 기능은 아직 준비중입니다.";
    }

    // 예약취소
    public String cancelReservation() {
        // 예약 취소 로직
        return "예약 취소 기능은 아직 준비중입니다.";
    }

    // 계정 삭제
    public String deleteAccount(String id) {
        int result = usersDao.deleteUser(id);

        if (result > 0) {
            return "계정이 성공적으로 삭제되었습니다.";
        } else {
            return "계정 삭제에 실패했습니다.";
        }
    }


    // 가맹점 문의
    public String requestFranchise() {
        // 가맹점 문의 로직
        return "가맹점 문의 기능은 아직 준비중입니다.";
    }
}
