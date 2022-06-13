package com.airtel.adtech.adaptor.sms;

import com.airtel.adtech.dto.mongo.AudienceMongoDTO;

import io.restassured.response.Response;

public interface AudeinceAdaptor {
	
	
	AudienceMongoDTO fetchAudienceById(String Id, String uniqueIdentifier);
	Response getAudienceCount(String uniqueIdentifier);
	

}
 