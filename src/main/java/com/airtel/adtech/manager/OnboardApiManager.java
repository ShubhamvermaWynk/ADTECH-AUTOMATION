package com.airtel.adtech.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import java.sql.Connection;

import io.restassured.http.Header;
import io.restassured.response.Response;

import com.airtel.adtech.adaptor.userOnboard.OnboardUserAdaptor;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserIAdaptormpl;
import com.airtel.adtech.dao.onboard.OnboardDao;
import com.airtel.adtech.dao.onboard.OnboardDaoImpl;
import com.airtel.common.variables.OrderGlobalVariables;
import com.airtel.teams.common.CommonApi;
import com.airtel.common.dto.response.AuthenticateResponseDTO;

public class OnboardApiManager  extends CommonApi  {
	
	CommonApi commonApi = new CommonApi();
	OnboardUserAdaptor onboard = new OnboardUserIAdaptormpl();
	OnboardDao onboardDao= new OnboardDaoImpl();

	public OnboardApiManager(Properties envPropertyFile) {
//		Properties iamDataPropertyFile = commonApi.getTestDataConfigPropertyObject("iam");

//		Map<String, Properties> testDataPropMap = new HashMap<>();
//		testDataPropMap.put("iam", iamDataPropertyFile);
//
//		commonApiManager = new CommonApiManager(envPropertyFile, testDataPropMap);
//	}
	}



	public OrderGlobalVariables createUserCompanyAdtech(OrderGlobalVariables orderGlobalVariables) {
		String userUUid = createUser(orderGlobalVariables.getIdentityServerInitials(),
				orderGlobalVariables.getTestConIam(), orderGlobalVariables.getTestCon(),
				orderGlobalVariables.getMobileNumberAd(), orderGlobalVariables.getEmailIdAd(),
				orderGlobalVariables.getFirstName(), orderGlobalVariables.getLastName(),
				orderGlobalVariables.getSignupPassword(), orderGlobalVariables.getPositiveAppName(),
				orderGlobalVariables.getUniqueidentifier(), orderGlobalVariables.getBrowser1(),
				orderGlobalVariables.getMachine1());

		Map<String, String> formparams = new HashMap<>();

		formparams.put("companyKycReqVo.docType", "PAN");

		Map<String, List<File>> multipartFile = new HashMap<>();
		List<File> fileList = new ArrayList<>();
		fileList.add(new File(orderGlobalVariables.getPanPathName()));

		multipartFile.put("fileList", fileList);

		Response registerCompanyResponse = onboard.registerSubmitKycDocuments(
				orderGlobalVariables.getOnboardingServerInitials(), formparams, multipartFile,
				orderGlobalVariables.getGstinAd(), userUUid, 200, orderGlobalVariables.getUniqueidentifier());

		JSONObject jsonObject = new JSONObject(registerCompanyResponse.asString());
		String companyUuid = jsonObject.getString("uuid");

		onboardDao.updateEmailVerified(orderGlobalVariables.getTestCon(), userUUid);

		orderGlobalVariables.setUserUUid(userUUid);
		orderGlobalVariables.setCompanyUuid(companyUuid);

		return orderGlobalVariables;
	}

	
	public String createUser(String authenticatorServerInitials, Connection connectionIam, Connection conOnboard,
			String mobileNumber, String emailId, String firstName, String lastName, String signupPassword,
			String appName, String uniqueidentifier, String browser, String machine) {

		deleteUserCompanyDataFromDb(connectionIam, conOnboard, mobileNumber, emailId);
		Map<String, String> requestData = prepareSignUpRequest(mobileNumber, emailId, firstName,
				lastName, signupPassword);
		Response response = onboard.signUpUser(authenticatorServerInitials, requestData, appName, 200,
				uniqueidentifier, browser, machine);
		AuthenticateResponseDTO validateAuthenticateOtpResponseDTO = convertFromJson(response.asString(),
				AuthenticateResponseDTO.class);
		String userId = validateAuthenticateOtpResponseDTO.getUserUuid();

		return userId;
	}

	
	
	public Map<String, String> prepareSignUpRequest(String mobileNumber, String emailId, String firstName,
			String lastName, String password) {

		Map<String, String> signUpRequest = new HashMap<>();
		if (mobileNumber != null)
			signUpRequest.put("mobileNumber", mobileNumber);
		if (emailId != null)
			signUpRequest.put("emailId", emailId);
		if (firstName != null)
			signUpRequest.put("firstName", firstName);
		if (lastName != null)
			signUpRequest.put("lastName", lastName);
		if (password != null)
			signUpRequest.put("password", password);

		return signUpRequest;
	}
	
	
	

	


	

	public void deleteUserCompanyDataFromDb(Connection connectionIam, Connection connectionOnboard, String mobileNumber,
			String emailId) {
		onboardDao.deleteCompanyDataFromDb(connectionOnboard, mobileNumber, emailId);

		
		onboardDao.deleteFromAdvertiser(connectionOnboard, mobileNumber, emailId);
		onboardDao.deleteFromUserIdentity(connectionIam, mobileNumber, emailId);
		onboardDao.deleteFromUserProfile(connectionOnboard, mobileNumber, emailId);
	}




	

}
