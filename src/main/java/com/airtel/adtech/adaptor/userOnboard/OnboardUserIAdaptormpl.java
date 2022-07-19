package com.airtel.adtech.adaptor.userOnboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.teams.common.CommonApi;

import io.restassured.http.Header;
import io.restassured.response.Response;

public class OnboardUserIAdaptormpl  extends CommonApi implements  OnboardUserAdaptor,AdtechApiConstants{

	
	
	
	
	@Override
	public Response signUpUser(String teamServerInitials, Map<String, String> requestData, String appId,
			int expectedStatusCode, String uniqueIdentifier, String browser, String machine) {
		Map<String, String> formParams = new HashMap<>();

		String mobileNumber = requestData.get("mobileNumber");
		if (mobileNumber != null) {
			formParams.put("mobileNumber", mobileNumber);
		}

		String emailId = requestData.get("emailId");
		if (emailId != null) {
			formParams.put("emailId", emailId);
		}

		String lastName = requestData.get("lastName");
		if (lastName != null) {
			formParams.put("lastName", lastName);
		}

		formParams.put("firstName", requestData.get("firstName"));
		formParams.put("password", requestData.get("password"));

		List<Header> requestHeader = new ArrayList<Header>();
		requestHeader.add(new Header("app-id", appId));
		requestHeader.add(new Header("browser", browser));
		requestHeader.add(new Header("machine", machine));

		return getPostResponseWithStatusCode(null, teamServerInitials, SIGN_UP_PATH, true, expectedStatusCode, null,
				requestHeader, uniqueIdentifier, formParams);
	}

	
	



	@Override
	public Response registerSubmitKycDocuments(String teamUserServerInitials, Map<String, String> formParams,
			Map<String, List<File>> multipartFile, String gstin, String userId, int expectedStatusCode,
			String uniqueIdentifier) {
		if (gstin != null) {
			formParams.put("gstinNumber", gstin);
		}

		List<Header> extraheaders = new ArrayList<>();
		if (userId != null) {
			extraheaders.add(new Header("USER-ID", userId));
		}

		return getPostResponseWithStatusCodeMultipart(null, teamUserServerInitials, REGISTER_COMPANY_PATH, true,
				expectedStatusCode, null, extraheaders, uniqueIdentifier, formParams, multipartFile);
	}
	
	public Response fetchCompanyDetails(String serverInitials, String code, String scope, String uniqueIdentifier) {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("code", code);
		queryParams.put("scope", scope);

		return getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams, FETCH_COMPANY_PATH, false, true,
				null, null, uniqueIdentifier);
	}

	public Response fetchUserByUuid(String serverInitials, String userUuid, String scope, String uniqueIdentifier) {
		List<Header> extraheaders = new ArrayList<>();
		extraheaders.add(new Header("USER-ID", userUuid));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("scope", scope);

		return getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams, FETCH_USER_BY_UUID_PATH, false,
				true, null, extraheaders, uniqueIdentifier);
	}

	public Response fetchCompanyDetailsByUuid(String serverInitials, String userUuid, String scope,
			String uniqueIdentifier) {
		List<Header> extraheaders = new ArrayList<>();
		extraheaders.add(new Header("USER-ID", userUuid));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("scope", scope);

		return getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams, FETCH_COMPANY_BY_UUID_PATH,
				false, true, null, extraheaders, uniqueIdentifier);
	}
	
	
	
	
}
