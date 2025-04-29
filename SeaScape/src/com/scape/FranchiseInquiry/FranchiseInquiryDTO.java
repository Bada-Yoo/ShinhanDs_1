package com.scape.FranchiseInquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class FranchiseInquiryDTO {
	private int INQUIRY_ID;
	private String USER_ID;   
	private int STATUS;    
}
