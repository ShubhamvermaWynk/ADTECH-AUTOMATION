package com.airtel.adtech.adaptor.userOnboard;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public interface OnboardUserAdaptor {
	
	Response signUpUser(String teamServerInitials, Map<String, String> requestData, String appId,
			int expectedStatusCode, String uniqueIdentifier, String browser, String machine);
	
	
	Response registerSubmitKycDocuments(String teamUserServerInitials, Map<String, String> formParams,
			Map<String, List<File>> multipartFile, String gstin, String userId, int expectedStatusCode,
			String uniqueIdentifier);
	Response fetchCompanyDetails(String serverInitials, String code, String scope, String uniqueIdentifier);
	Response fetchUserByUuid(String serverInitials, String userUuid, String scope, String uniqueIdentifier);
	Response fetchCompanyDetailsByUuid(String serverInitials, String userUuid, String scope,
			String uniqueIdentifier);
}
