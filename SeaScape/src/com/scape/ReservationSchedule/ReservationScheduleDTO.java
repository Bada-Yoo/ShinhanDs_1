package com.scape.ReservationSchedule;


import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationScheduleDTO {
	private int SCHEDULE_ID;     
	private String ROOM_ID;         
	private String USER_ID;         
	private Date RESERVATION_DATE;
	private String RESERVATION_TIME;
	private int HEADCOUNT;       
	private String IS_RESERVED;    
}
