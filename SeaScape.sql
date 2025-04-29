-- 1. 제작자        
CREATE TABLE Creator (
    creator_id VARCHAR2(50) PRIMARY KEY,
    creator_pw VARCHAR2(50) NOT NULL,
    creator_nickname VARCHAR2(50) NOT NULL
);

-- 2. 유저
CREATE TABLE Users (
    user_id VARCHAR2(50) PRIMARY KEY,
    user_pw VARCHAR2(50) NOT NULL,
    user_name VARCHAR2(50) NOT NULL,
    user_nickname VARCHAR2(50) NOT NULL,
    user_age NUMBER NOT NULL,
    escape_history NUMBER
);

-- 3. ㅁ장
CREATE TABLE Store (
    store_unique_id VARCHAR2(50) PRIMARY KEY,
    store_id VARCHAR2(50) NOT NULL,
    store_pw VARCHAR2(50) NOT NULL,
    location VARCHAR2(50) NOT NULL,
    size_limit NUMBER NOT NULL
);

-- 4. 방탈출테마
CREATE TABLE Room (
    room_id VARCHAR2(50) NOT NULL,
    creator_id VARCHAR2(50) NOT NULL,
    room_name VARCHAR2(100) NOT NULL,
    genre VARCHAR2(50),
    is_19 NUMBER NOT NULL,
    price NUMBER NOT NULL,
    limit_time NUMBER NOT NULL,
    store_unique_id VARCHAR2(50), -- NULL 허용
    synopsis VARCHAR2(200),
    CONSTRAINT PK_Room PRIMARY KEY (room_id, creator_id),
    CONSTRAINT FK_Room_Creator FOREIGN KEY (creator_id) REFERENCES Creator(creator_id),
    CONSTRAINT FK_Room_Store FOREIGN KEY (store_unique_id) REFERENCES Store(store_unique_id)
);

-- 5. 가맹점문의
CREATE TABLE FranchiseInquiry (
    inquiry_id NUMBER NOT NULL,
    user_id VARCHAR2(50) NOT NULL,
    status NUMBER,
    CONSTRAINT PK_FranchiseInquiry PRIMARY KEY (inquiry_id),
    CONSTRAINT FK_Franchise_User FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 6. 예약스케쥴
CREATE TABLE ReservationSchedule (
    schedule_id NUMBER NOT NULL,
    room_id VARCHAR2(50) NOT NULL,
    user_id VARCHAR2(50),
    reservation_date DATE NOT NULL,
    reservation_time VARCHAR2(5) NOT NULL,
    headcount NUMBER NOT NULL,
    is_reserved VARCHAR2(1) NOT NULL,
    CONSTRAINT PK_ReservationSchedule PRIMARY KEY (schedule_id),
    CONSTRAINT FK_Resv_Room FOREIGN KEY (room_id) REFERENCES Room(room_id),
    CONSTRAINT FK_Resv_User FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
