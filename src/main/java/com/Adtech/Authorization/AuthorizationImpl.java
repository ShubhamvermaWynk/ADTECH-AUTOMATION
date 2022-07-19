package com.Adtech.Authorization;

import java.util.List;
import java.util.Map;

import com.airtel.teams.common.CommonApi;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class AuthorizationImpl extends CommonApi implements Authorization{

	
	@Override
	public Response getAuthorizationResponce(Map<String,String> query,List<Header> header,ContentType contentType, String Url, Boolean CheckStatus) {
		 
		
		 return postResponseWithHeaders(null, header, contentType, Url, CheckStatus);
	}

	@Override
	public String getAuthorizationToken(Map<String, String> query, List<Header> header, ContentType contentType,
			String Url, Boolean CheckStatus) {
		
		Response res = postResponseWithHeaders(null, header, contentType, Url, CheckStatus);
      
		return res.getHeaders().get("x-auth-token").toString();
	}
	
}
