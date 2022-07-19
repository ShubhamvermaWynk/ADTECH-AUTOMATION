package com.Adtech.adaptor.personalization;

import io.restassured.response.Response;

public interface PersonlaizationAdaptor {
	
	Response getOrderList(String userId);
	Response vmaxCampiagndata();
	Response vmaxtoken();
	

}
