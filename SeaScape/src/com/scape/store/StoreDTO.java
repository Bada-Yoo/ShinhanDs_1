package com.scape.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreDTO {
	 private String STORE_UNIQUE_ID;
	 private String STORE_ID;       
	 private String STORE_PW;       
	 private String LOCATION;       
	 private int SIZE_LIMIT;
}
