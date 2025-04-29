package com.scape.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoomDTO {
	private String ROOM_ID;        
	private String CREATOR_ID;     
	private String ROOM_NAME;      
	private String GENRE;          
	private int  IS_19;          
	private int PRICE;          
	private int LIMIT_TIME;     
	private String STORE_UNIQUE_ID;
	private String SYNOPSIS;       
}
