package com.Adtech.Authorization;

import java.util.List;
import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public interface Authorization {
	

	String getAuthorizationToken(Map<String,String> query,List<Header> header, ContentType contentType, String Url, Boolean CheckStatus);

	Response getAuthorizationResponce(Map<String, String> query, List<Header> header, ContentType contentType,
			String Url, Boolean CheckStatus);

}
