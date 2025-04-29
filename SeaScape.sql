-- 1. 제작자 테이블
CREATE TABLE Creator (
    creator_id        VARCHAR2(50) NOT NULL,
    creator_pw        VARCHAR2(50) NOT NULL,
    creator_nickname  VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_Creator PRIMARY KEY (creator_id)
);

-- 2. 유저 테이블
CREATE TABLE Users (
    user_id         VARCHAR2(50) NOT NULL,
    user_pw         VARCHAR2(50) NOT NULL,
    user_name       VARCHAR2(50) NOT NULL,
    user_nickname   VARCHAR2(50) NOT NULL,
    user_age        NUMBER       NOT NULL,
    escape_history  NUMBER,
    CONSTRAINT PK_User PRIMARY KEY (user_id)
);

-- 3. 매장 테이블
CREATE TABLE Store (
    store_unique_id  VARCHAR2(50) NOT NULL,
    store_id         VARCHAR2(50) NOT NULL,
    store_pw         VARCHAR2(50) NOT NULL,
    location         VARCHAR2(50) NOT NULL,
    size_limit       NUMBER       NOT NULL,
    CONSTRAINT PK_Store PRIMARY KEY (store_unique_id)
);

-- 4. 방탈출테마 테이블
CREATE TABLE Room (
    room_id         VARCHAR2(50) NOT NULL,
    creator_id      VARCHAR2(50) NOT NULL,
    room_name       VARCHAR2(100) NOT NULL,
    genre           VARCHAR2(50),
    is_19           NUMBER       NOT NULL,
    price           NUMBER       NOT NULL,
    limit_time      NUMBER       NOT NULL,
    store_unique_id VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_Room PRIMARY KEY (room_id, creator_id),
    CONSTRAINT FK_Room_Creator FOREIGN KEY (creator_id)
        REFERENCES Creator (creator_id),
    CONSTRAINT FK_Room_Store FOREIGN KEY (store_unique_id)
        REFERENCES Store (store_unique_id)
);

-- 5. 가맹점 문의 테이블
CREATE TABLE FranchiseInquiry (
    inquiry_id   NUMBER         NOT NULL,
    user_id      VARCHAR2(50)   NOT NULL,
    status       NUMBER,
    CONSTRAINT PK_FranchiseInquiry PRIMARY KEY (inquiry_id),
    CONSTRAINT FK_Franchise_User FOREIGN KEY (user_id)
        REFERENCES Users (user_id)
);

-- 6. 예약스케쥴 테이블
CREATE TABLE ReservationSchedule (
    schedule_id       NUMBER        NOT NULL,
    room_id           VARCHAR2(50)  NOT NULL,
    user_id           VARCHAR2(50),
    reservation_date  DATE          NOT NULL,
    reservation_time  VARCHAR2(5)   NOT NULL,  -- 예: '14:00'
    headcount         NUMBER        NOT NULL,
    is_reserved       VARCHAR2(1)   NOT NULL,  -- 'Y' or 'N'
    CONSTRAINT PK_ReservationSchedule PRIMARY KEY (schedule_id),
    CONSTRAINT FK_Resv_Room FOREIGN KEY (room_id)
        REFERENCES Room (room_id),
    CONSTRAINT FK_Resv_User FOREIGN KEY (user_id)
        REFERENCES Users (user_id)
);
ALTER TABLE Room ADD CONSTRAINT UQ_room_id UNIQUE (room_id);
ALTER TABLE Room ADD synopsis VARCHAR2(200);
ALTER TABLE Room MODIFY store_unique_id VARCHAR2(50);
