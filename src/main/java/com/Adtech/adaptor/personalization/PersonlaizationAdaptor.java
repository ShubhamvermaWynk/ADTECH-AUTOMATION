package com.Adtech.adaptor.personalization;

import io.restassured.response.Response;

public interface PersonlaizationAdaptor {
	//Comment
	
	Response getOrderList(String userId);
	Response vmaxCampiagndata();
	Response vmaxtoken();
	

}
