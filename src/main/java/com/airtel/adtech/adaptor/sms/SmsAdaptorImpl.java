package com.airtel.adtech.adaptor.sms;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.request.sms.AdminCampaignApprovalDTO;
import com.airtel.adtech.dto.request.sms.AdminDLTApprovalDTO;
import com.airtel.adtech.dto.request.sms.SubmitAudienceWithRadiusDTO;
import com.airtel.adtech.dto.request.sms.SubmitAudienceWithRadiusDTO.Geo;
import com.airtel.adtech.dto.request.sms.SubmitAudienceWithRadiusDTO.Radius;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.request.sms.SubmitDLTwithPeidDTO;
import com.airtel.adtech.dto.request.sms.TestSMSDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.dto.response.sms.FetchTargetandBudgetDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderListDTO;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.teams.common.CommonApi;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class SmsAdaptorImpl extends CommonApi implements SmsAdaptor, AdtechApiConstants {

	String serverInitials;

	public SmsAdaptorImpl(Properties envProperty) {
		serverInitials = envProperty.getProperty("ADTECH_SMS");

	}

	CommonApi commonApi = new CommonApi();

	@Override
	public Response sendOTP(String uniqueIdentifer, String companyUuid, String userId, String type) {

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("type", type);

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				COMMON_DLT + companyUuid + "/otp", false, false, null, extraHeaders, uniqueIdentifer);

		return response;

	}

	@Override
	public Response validateOTP(String uniqueIdentifer, String companyUuid, String type, int expectedStatus) {

		Map<String, String> obj = new HashMap<>();
		obj.put("otp", "1234");

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("type", type);

		String body = convertToJson(obj);

		Response response = getPostResponseWithStatusCodeandParams(body, serverInitials,
				COMMON_DLT + companyUuid + "/otp", false, expectedStatus, null, null, queryParams, uniqueIdentifer);

		return response;
	}

	@Override
	public Response registerDLTwithPan(String companyUuid, String userId, UserCompanyResponseVo userCompany,
			String panPath, OrganisationType orgType, boolean acceptedFlag, int expectedStatusCode,
			String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Map<String, String> formparams = new HashMap<>();

		formparams.put("name", userCompany.getUser().getFirstName() + userCompany.getUser().getLastName());
		formparams.put("emailId", userCompany.getUser().getEmail());
		formparams.put("mobileNumber", userCompany.getUser().getMobile());
		formparams.put("companyName", userCompany.getCompany().getTradeName());
		formparams.put("companyType", String.valueOf(orgType));
		formparams.put("pan", userCompany.getCompany().getPanNumber());
		formparams.put("gstn", userCompany.getCompany().getAddresses().get(0).getGstinNumber());

		formparams.put("accepted", String.valueOf(acceptedFlag));

		Map<String, List<File>> multipartFile = new HashMap<>();
		List<File> fileList = new ArrayList<>();
		fileList.add(new File(panPath));

		multipartFile.put("panDocument", fileList);
		multipartFile.put("gstDocument", Arrays.asList(new File(panPath)));
		multipartFile.put("proofEntityDocument", Arrays.asList(new File(panPath)));
		multipartFile.put("proofIdentityDocument", Arrays.asList(new File(panPath)));

		Response response = getPostResponseWithStatusCodeMultipart(null, serverInitials,
				COMMON_DLT + companyUuid + "/v1/registration", false, expectedStatusCode, null, extraHeaders,
				uniqueIdentifer, formparams, multipartFile);

		return response;
	}

	
	
	@Override
	public Response reviseDLTwithPan(String companyUuid, String userId, UserCompanyResponseVo userCompany,String panPath,  OrganisationType orgType, boolean acceptedFlag, int expectedStatusCode,String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Map<String, String> formparams = new HashMap<>();

		formparams.put("name", userCompany.getUser().getFirstName() + userCompany.getUser().getLastName());
		formparams.put("emailId", userCompany.getUser().getEmail());
		formparams.put("mobileNumber", userCompany.getUser().getMobile());
		formparams.put("companyName", userCompany.getCompany().getTradeName());
		formparams.put("companyType", String.valueOf(orgType));
		formparams.put("pan", userCompany.getCompany().getPanNumber());
		formparams.put("gstn", userCompany.getCompany().getAddresses().get(0).getGstinNumber());

		formparams.put("accepted", String.valueOf(acceptedFlag));

		Map<String, List<File>> multipartFile = new HashMap<>();
		List<File> fileList = new ArrayList<>();
		fileList.add(new File(panPath));

		multipartFile.put("panDocument", fileList);
		multipartFile.put("gstDocument", Arrays.asList(new File(panPath)));
		multipartFile.put("proofEntityDocument", Arrays.asList(new File(panPath)));
		multipartFile.put("proofIdentityDocument", Arrays.asList(new File(panPath)));

		

		Response response = getPostResponseWithStatusCodeMultipart(null, serverInitials,
				COMMON_DLT + companyUuid + "/revised/registration", false, expectedStatusCode, null, extraHeaders,
				uniqueIdentifer, formparams, multipartFile);

		return response;
	}
	
	
	@Override
	public FetchCampaignDTO fetchCampaign(String companyUuid, String userId, String campaignId,
			String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("campaignId", campaignId);

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				SUBMIT_CAMPAIGN + companyUuid + "/", false, false, null, extraHeaders, uniqueIdentifer);

		FetchCampaignDTO campaignResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				FetchCampaignDTO.class);

		return campaignResponse;

	}

	@Override
	public FetchDLTInfoDTO fetchDLT(String companyUuid, String userId, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));
		extraHeaders.add(new Header("companyUuid", companyUuid));

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				COMMON_DLT + companyUuid + "/v1/businessDetails", false, false, null, extraHeaders, uniqueIdentifer);

		FetchDLTInfoDTO fetchDltResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				FetchDLTInfoDTO.class);

		return fetchDltResponse;

	}

	@Override
	public FetchIndustryDTO fetchIndustry(String uniqueIdentifer) {

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null, FETCH_INDUSTRY_SMS, false,
				false, null, null, uniqueIdentifer);

		FetchIndustryDTO fetchDltResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				FetchIndustryDTO.class);

		return fetchDltResponse;

	}

	@Override
	public Response fetchContract(String companyUuid, String userId, String type, String uniqueIdentifer) {
		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("type", type);

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				COMMON_DLT + companyUuid + "/v1/loa", false, false, null, extraHeaders, uniqueIdentifer);
		return response;
	}

	@Override
	public FetchTargetandBudgetDTO submitBudgetandTarget(double lat, double longitude, long radius, String industry,
			int expectedStatusCode, String companyUUid, String userId, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		SubmitAudienceWithRadiusDTO targetObj = new SubmitAudienceWithRadiusDTO();

		SubmitAudienceWithRadiusDTO.Geo geoObject = new Geo();

		SubmitAudienceWithRadiusDTO.Radius radiusObj = new Radius();

		radiusObj.setUnit("METERS");
		radiusObj.setValue(radius);

		geoObject.setLatitude(lat);
		geoObject.setLongitude(longitude);

		targetObj.setIndustry(Arrays.asList(industry));
		targetObj.setGeoLocation(geoObject);
		targetObj.setRadius(radiusObj);

		String body = convertToJson(targetObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, FETCH_TARGETING + "/" + companyUUid,
				true, expectedStatusCode, null, extraHeaders, uniqueIdentifer);

		FetchTargetandBudgetDTO budgetResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				FetchTargetandBudgetDTO.class);

		return budgetResponse;

	}

	@Override
	public SmsOrderListDTO fetchOrderList(String companyUuid, String userId, String uniqueIdentifer) {
		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				FETCH_ORDER_LIST_SMS + companyUuid + "/", false, false, null, extraHeaders, uniqueIdentifer);

		SmsOrderListDTO campaignResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				SmsOrderListDTO.class);

		return campaignResponse;
	}

	@Override
	public Response registerDLTwithPeid(String companyUuid, String userId, String peId, String headerId,
			String template, String templateId, int expectedStatusCode, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));
		extraHeaders.add(new Header("companyUuid", companyUuid));

		SubmitDLTwithPeidDTO dltObj = new SubmitDLTwithPeidDTO();
		dltObj.setPeId(peId);
		dltObj.setHeaderId(headerId);
		dltObj.setTemplate(template);
		dltObj.setTemplateId(templateId);

		String body = convertToJson(dltObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials,
				COMMON_DLT + companyUuid + "/validateAndSubmit", true, expectedStatusCode, null, extraHeaders,
				uniqueIdentifer);
		
		return response;
	}

	@Override
	public FetchCampaignDTO submitCampaign(String companyUuid, String userId, SubmitCampaignDTO campaignObj,
			String campaignId, boolean submitFlag, int expectedStatusCode, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("finalSubmit", String.valueOf(submitFlag));
		if (campaignId != null) {
			queryParams.put("campaignId", campaignId);
		}

		String body = convertToJsonwithObjectMapper(campaignObj);

		Response response = patchResponseWithStatusCode(body, serverInitials, SUBMIT_CAMPAIGN + companyUuid + "/", true,
				expectedStatusCode, null, extraHeaders, queryParams, uniqueIdentifer);

		
		FetchCampaignDTO campaignResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				FetchCampaignDTO.class);

		return campaignResponse;

	}

	@Override
	public Response approveDLT(String campaignId, String headerId, String templateId, String template, String peId,
			String companyuuid, int expectedStatusCode, String uniqueIdentifer) {

		AdminDLTApprovalDTO dltObj = new AdminDLTApprovalDTO();

		dltObj.setCampaignId(campaignId);
		dltObj.setPeId(peId);
		dltObj.setHeaderId(headerId);
		dltObj.setTemplate(template);
		dltObj.setTemplateId(templateId);
		dltObj.setCompanyUuid(companyuuid);

		String body = convertToJson(dltObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, APPROVE_DLT, true, expectedStatusCode,
				null, null, uniqueIdentifer);

		return response;

	}

	@Override
	public Response approveCampaign(String campaignId, String templateId, String template, int expectedStatusCode,
			String uniqueIdentifer) {

		AdminCampaignApprovalDTO camapignObj = new AdminCampaignApprovalDTO();
		camapignObj.setTemplate(template);
		camapignObj.setCampaignId(campaignId);
		camapignObj.setTemplateId(templateId);

		String body = convertToJson(camapignObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, APPROVE_CAMPAIGN, true,
				expectedStatusCode, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response approveCampaignbyId(String campaignId, int expectedStatusCode, String uniqueIdentifer) {

		Map<String, String> campaign = new HashMap<>();
		campaign.put("campaignId", campaignId);

		String body = convertToJson(campaign);

		Response response = getPostResponseWithStatusCode(body, serverInitials, APPROVE_CAMPAIGN_BYID, true,
				expectedStatusCode, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public GetAnalyticsMongoDTO getAnalyticsById(String campaignId, int expectedStatusCode, String uniqueIdentifer) {

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("campaignId", campaignId);

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams, FETCH_ANALYTICS,
				false, false, null, null, uniqueIdentifer);

		GetAnalyticsMongoDTO campaignResponse = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				GetAnalyticsMongoDTO.class);

		return campaignResponse;
	}

	@Override
	public Response registerHeaders(String companyUuid, String userId, String peId, String headerId, String template,
			String templateId, int expectedStatusCode, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));
		extraHeaders.add(new Header("companyUuid", companyUuid));

		SubmitDLTwithPeidDTO dltObj = new SubmitDLTwithPeidDTO();
		dltObj.setPeId(peId);
		dltObj.setHeaderId(headerId);
		dltObj.setTemplate(template);
		dltObj.setTemplateId(templateId);

		String body = convertToJson(dltObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, COMMON_DLT + companyUuid + "/manage",
				true, expectedStatusCode, null, extraHeaders, uniqueIdentifer);

		return response;
	}

//	Test SMS DTO

	@Override
	public Response testSMS(String userId, String companyId, String phoneNumber, String template, String templateId,
			String headerId, int expectedStatusCode, String uniqueIdentifer) {

		List<Header> headers = new ArrayList<>();
		headers.add(new Header("USER-ID", userId));

		TestSMSDTO testSms = new TestSMSDTO();
		testSms.setPhoneNumber(phoneNumber);
		testSms.setTemplate(template);
		testSms.setTemplateId(templateId);
		testSms.setHeaderId(headerId);

		String body = convertToJson(testSms);

		Response response = getPostResponseWithStatusCode(body, serverInitials, TEST_SMS_PATH + companyId, false,
				expectedStatusCode, null, headers, uniqueIdentifer);

		return response;
	}

}
