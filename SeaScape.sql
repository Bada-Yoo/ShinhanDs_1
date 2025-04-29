CREATE TABLE `개발자` (
	`creator_id`	varchar2(50)	NOT NULL,
	`creator_pw`	varchar2(50)	NOT NULL,
	`creator_nickname`	varchar2(50)	NOT NULL,
	`creator_account`	number	NULL
);

CREATE TABLE `매장 본부사장` (
	`charge_id`	varchar2(50)	NOT NULL,
	`charge_account`	number	NULL
);

CREATE TABLE `예약` (
	`reservation_id`	number	NOT NULL,
	`user_id`	varchar2(50)	NOT NULL,
	`room_id`	varchar2(50)	NOT NULL,
	`store_unique_id`	varchar2(50)	NOT NULL,
	`Key`	number	NOT NULL,
	`reservation_date`	date	NOT NULL,
	`reservation_time`	date	NOT NULL,
	`headcount`	number	NOT NULL
);

CREATE TABLE `리뷰` (
	`review_id`	number	NOT NULL,
	`user_id`	varchar2(50)	NOT NULL,
	`user_nickname`	varchar2(50)	NOT NULL,
	`room_name`	varchar2(100)	NOT NULL,
	`time_left`	number	NULL,
	`hint_count`	number	NULL,
	`rating`	number(2,1)	NULL
);

CREATE TABLE `User` (
	`user_id`	varchar2(50)	NOT NULL,
	`user_pw`	varchar2(50)	NOT NULL,
	`user_name`	varchar2(50)	NOT NULL,
	`user_nickname`	varchar2(50)	NOT NULL,
	`user_age`	number	NOT NULL,
	`escape_history`	number	NULL
);

CREATE TABLE `방탈출` (
	`room_id`	varchar2(50)	NOT NULL,
	`creator_id`	varchar2(50)	NOT NULL,
	`room_name`	varchar2(100)	NOT NULL,
	`genre`	varchar2(50)	NULL,
	`is_19`	number	NOT NULL,
	`price`	number	NOT NULL,
	`limit_time`	number	NOT NULL,
	`Total_status`	number(3,2)	NULL,
	`store_unique_id`	varchar2(50)	NOT NULL
);

CREATE TABLE `매장` (
	`store_unique_id`	varchar2(50)	NOT NULL,
	`charge_id`	varchar2(50)	NOT NULL,
	`store_id`	varchar2(50)	NOT NULL,
	`store_pw`	varchar2(50)	NOT NULL,
	`room_id`	varchar2(50)	NOT NULL,
	`location`	varchar2(50)	NOT NULL,
	`size_limit`	number	NOT NULL,
	`store_account`	number	NULL
);

CREATE TABLE `가맹점문의` (
	`inquiry_id`	number	NOT NULL,
	`user_id`	varchar2(50)	NOT NULL,
	`status`	number	NULL
);

CREATE TABLE `예약시간스케쥴` (
	`Key`	number	NOT NULL,
	`room_id`	varchar2(50)	NOT NULL,
	`store_id`	number	NOT NULL,
	`reservation_date`	date	NOT NULL,
	`reservation_time`	date	NOT NULL,
	`is_reserved`	varchar2(50)	NOT NULL
);

ALTER TABLE `개발자` ADD CONSTRAINT `PK_개발자` PRIMARY KEY (
	`creator_id`
);

ALTER TABLE `매장 본부사장` ADD CONSTRAINT `PK_매장 본부사장` PRIMARY KEY (
	`charge_id`
);

ALTER TABLE `예약` ADD CONSTRAINT `PK_예약` PRIMARY KEY (
	`reservation_id`,
	`user_id`,
	`room_id`,
	`store_unique_id`,
	`Key`
);

ALTER TABLE `리뷰` ADD CONSTRAINT `PK_리뷰` PRIMARY KEY (
	`review_id`,
	`user_id`
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `방탈출` ADD CONSTRAINT `PK_방탈출` PRIMARY KEY (
	`room_id`,
	`creator_id`
);

ALTER TABLE `매장` ADD CONSTRAINT `PK_매장` PRIMARY KEY (
	`store_unique_id`,
	`charge_id`
);

ALTER TABLE `가맹점문의` ADD CONSTRAINT `PK_가맹점문의` PRIMARY KEY (
	`inquiry_id`,
	`user_id`
);

ALTER TABLE `예약시간스케쥴` ADD CONSTRAINT `PK_예약시간스케쥴` PRIMARY KEY (
	`Key`,
	`room_id`
);

ALTER TABLE `예약` ADD CONSTRAINT `FK_User_TO_예약_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `User` (
	`user_id`
);

ALTER TABLE `예약` ADD CONSTRAINT `FK_방탈출_TO_예약_1` FOREIGN KEY (
	`room_id`
)
REFERENCES `방탈출` (
	`room_id`
);

ALTER TABLE `예약` ADD CONSTRAINT `FK_매장_TO_예약_1` FOREIGN KEY (
	`store_unique_id`
)
REFERENCES `매장` (
	`store_unique_id`
);

ALTER TABLE `예약` ADD CONSTRAINT `FK_예약시간스케쥴_TO_예약_1` FOREIGN KEY (
	`Key`
)
REFERENCES `예약시간스케쥴` (
	`Key`
);

ALTER TABLE `리뷰` ADD CONSTRAINT `FK_User_TO_리뷰_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `User` (
	`user_id`
);

ALTER TABLE `방탈출` ADD CONSTRAINT `FK_개발자_TO_방탈출_1` FOREIGN KEY (
	`creator_id`
)
REFERENCES `개발자` (
	`creator_id`
);

ALTER TABLE `매장` ADD CONSTRAINT `FK_매장 본부사장_TO_매장_1` FOREIGN KEY (
	`charge_id`
)
REFERENCES `매장 본부사장` (
	`charge_id`
);

ALTER TABLE `가맹점문의` ADD CONSTRAINT `FK_User_TO_가맹점문의_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `User` (
	`user_id`
);

ALTER TABLE `예약시간스케쥴` ADD CONSTRAINT `FK_방탈출_TO_예약시간스케쥴_1` FOREIGN KEY (
	`room_id`
)
REFERENCES `방탈출` (
	`room_id`
);

