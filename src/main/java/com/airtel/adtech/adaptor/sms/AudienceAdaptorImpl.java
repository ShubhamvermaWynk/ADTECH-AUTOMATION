package com.airtel.adtech.adaptor.sms;

import java.util.Properties;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.teams.common.CommonApi;

import io.restassured.response.Response;

public class AudienceAdaptorImpl   extends CommonApi  implements AudeinceAdaptor, AdtechApiConstants{

	CommonApi commonApi= new CommonApi();
	String serverInitials;
	
	
	public AudienceAdaptorImpl(Properties envProperty) {
		serverInitials = envProperty.getProperty("AUDIENCE_MANAGER");
		
	}
	
	@Override
	public AudienceMongoDTO fetchAudienceById(String Id, String uniqueIdentifier) {
		
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				FETCH_AUDIENCE + Id , false, false, null,null , uniqueIdentifier);
	
		AudienceMongoDTO  audienceResponse= commonApi
				.convertFromJsonwithObjectMapperwithclass(response.asString(), AudienceMongoDTO.class);
		
		return audienceResponse;
	
		
		
	}

	@Override
	public Response getAudienceCount(String uniqueIdentifier) {
		
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				GET_AUDIENCE_COUNT , false, false, null,null , uniqueIdentifier);
	
		
		
		return response;
	}

}
