package com.Adtech.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.Adtech.dto.personalization.AdspotCampaignDTO;
import com.Adtech.dto.personalization.GetOrderListRequestDTO;
import com.Adtech.dto.personalization.UserDetailsDTO;
import com.common.utils.CommonApi;

public class PersonalizationApiManager {
	
	CommonApi commonApi= new CommonApi();
	String campaignsList1,campaignsList2,campaignsList3;
	
	public PersonalizationApiManager()
	{
		Properties testdata= commonApi.getTestDataConfigPropertyObject("personalization");
		campaignsList1= testdata.getProperty("CAMPAIGN1_LIST");
		campaignsList2= testdata.getProperty("CAMPAIGN2_LIST");
		campaignsList3= testdata.getProperty("CAMPAIGN3_LIST");
		
	}
	
	public GetOrderListRequestDTO setBody(String userId)
	{
		GetOrderListRequestDTO getObj= new GetOrderListRequestDTO();

		
		Map<String, AdspotCampaignDTO>  adspotObj= new HashMap<>();
		
		adspotObj.put("adspot1", requestObj(Arrays.asList(campaignsList1)));
		adspotObj.put("adspot2", requestObj(Arrays.asList(campaignsList2)));
		adspotObj.put("adspot3", requestObj(Arrays.asList(campaignsList3)));
		
		UserDetailsDTO userObj= new UserDetailsDTO ();
		userObj.setUid(userId);
		
		
		getObj.setAdspots(adspotObj);
		getObj.setUser(userObj);
		
		return getObj;
	}
	
	public AdspotCampaignDTO requestObj(List<String> campaigns)
	{
		AdspotCampaignDTO adObj= new AdspotCampaignDTO();
		adObj.setCampaigns(campaigns);
		return adObj;
		
	}
	

}
