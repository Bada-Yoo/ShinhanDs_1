package com.scape.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreatorDTO {
	private String CREATOR_ID;      
	private String CREATOR_PW;      
	private String CREATOR_NICKNAME;
}
