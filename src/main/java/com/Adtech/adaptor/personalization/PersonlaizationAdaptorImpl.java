package com.Adtech.adaptor.personalization;

import java.util.Properties;

import com.Adtech.constants.AdtechApiConstants;
import com.Adtech.dto.personalization.GetOrderListRequestDTO;
import com.Adtech.manager.PersonalizationApiManager;
import com.common.utils.CommonApi;

import io.restassured.response.Response;

public class PersonlaizationAdaptorImpl   extends CommonApi implements PersonlaizationAdaptor, AdtechApiConstants {

	String serverInitials;
	PersonalizationApiManager personaliObj= new PersonalizationApiManager();
	

	public PersonlaizationAdaptorImpl(Properties envProperty) {
		serverInitials = envProperty.getProperty("PERSONALIZATION_URL");
		
	} 

	@Override
	public Response getOrderList(String userId) {
		
		GetOrderListRequestDTO getOrderObj=personaliObj.setBody(userId);
		String body= convertToJson(getOrderObj);
		
		Response response= getPostResponse(body, serverInitials,GET_ORDER_LIST , false, null);
	
		return response;
	}

	@Override
	public Response vmaxCampiagndata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response vmaxtoken() {
		// TODO Auto-generated method stub
		return null;
	}
}
