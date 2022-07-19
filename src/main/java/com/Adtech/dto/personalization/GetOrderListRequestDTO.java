package com.Adtech.dto.personalization;


import java.util.Map;

import lombok.Data;
@Data
public class GetOrderListRequestDTO {

	private Map<String, AdspotCampaignDTO> adspots;
	private UserDetailsDTO user;
	 
	
	

}
