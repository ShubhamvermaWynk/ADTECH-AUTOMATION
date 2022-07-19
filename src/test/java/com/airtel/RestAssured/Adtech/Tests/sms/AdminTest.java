package com.airtel.RestAssured.Adtech.Tests.sms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;

import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.Properties;

import java.util.stream.Collectors;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.airtel.adtech.adaptor.sms.AdminAdaptor;
import com.airtel.adtech.adaptor.sms.AdminAdaptorImpl;
import com.airtel.adtech.adaptor.sms.SmsAdaptor;
import com.airtel.adtech.adaptor.sms.SmsAdaptorImpl;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserAdaptor;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserIAdaptormpl;
import com.airtel.adtech.constants.enums.AdminOrderListKey;
import com.airtel.adtech.constants.enums.AdminOrderSortByKey;
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.SortDirection;
import com.airtel.adtech.constants.enums.TemplateStatus;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.request.sms.AdminOrderListDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.AdminOrderListResponseDTO;
import com.airtel.adtech.dto.response.sms.AdminPartnerDetailsDTO;
import com.airtel.adtech.dto.response.sms.AdminTagPeidDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;

import com.airtel.adtech.manager.AdminAdtechApiManager;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.AdminAdMongo;
import com.airtel.adtech.mongo.sms.AdminAdMongoImpl;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;
import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;

import io.restassured.response.Response;

public class AdminTest extends ReportHelper {
	CommonApi commonApi = new CommonApi();

	
	OnboardApiManager onboardApiManager;

	OrderGlobalVariables orderGlobalVariables;
	

	String uniqueIdentifier;
	Connection billingCon;
	SmsAdtechApiManager apiManger;
	SmsAdaptor smsadaptor;
	SmsAdMongo smsMongo;

	Properties smsDataPropertyFile;
	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;

	String peId, headerId, templateId, template, templateURL;
	UserCompanyResponseVo userCompanyResponse;
	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	List<String> industriesKeys;
	List<String> industriesValues;
	Instant date;
	String serverInitials;
	String linkIgnoreJson;

	AdminAdtechApiManager apiAdminManager;
	AdminAdaptor adminAdaptor;
	static String dltCampaignStatus = "SUBMITTED";
	AdminAdMongo adminMongo;
	String dltJsonIgnore;

	String templateIds, headerIds;
	String templatesWithSameHeader, title;
	String revisePanPath;
	OnboardUserAdaptor onboard;
	ITestContext context;
	
	@Parameters({ "environment" })
	@BeforeClass
	public void setEnvironment(@Optional("qa_1") String environment) {

		
		Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
		
		onboard= new OnboardUserIAdaptormpl();
		onboardApiManager= new OnboardApiManager(envPropertyFile);
		smsDataPropertyFile = commonApi.getTestDataConfigPropertyObject("adtechSms");

		serverInitials = envPropertyFile.getProperty("USERS_URL");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);
		
		uniqueIdentifier = CommonApi.uniqueidentifier();

		smsadaptor = new SmsAdaptorImpl(envPropertyFile);
		smsMongo = new SmsAdMongoImpl(envPropertyFile);
		adminMongo = new AdminAdMongoImpl(envPropertyFile);
		apiManger = new SmsAdtechApiManager(envPropertyFile, smsDataPropertyFile);

		peId = smsDataPropertyFile.getProperty("PEID");
		headerId = smsDataPropertyFile.getProperty("HEADER_ID");
		templateId = smsDataPropertyFile.getProperty("TEMPLATEID");
		template = smsDataPropertyFile.getProperty("TEMPLATE");
		templateURL = smsDataPropertyFile.getProperty("TEMPLATE_WITH_URL");
		title = smsDataPropertyFile.getProperty("CAMPAIGN_NAME");

		dltJsonIgnore = smsDataPropertyFile.getProperty("DLT_JSON_IGNORE");

		lat = Double.valueOf(smsDataPropertyFile.getProperty("LAT"));
		longi = Double.valueOf(smsDataPropertyFile.getProperty("LONGI"));
		radius = Long.valueOf(smsDataPropertyFile.getProperty("RADIUS"));
		audienceReachCount = Long.valueOf(smsDataPropertyFile.getProperty("REACH_COUNT"));
		budget = new BigDecimal(smsDataPropertyFile.getProperty("BUDGET"));

		date = Instant.now().plus(2, ChronoUnit.DAYS);

		linkIgnoreJson = smsDataPropertyFile.getProperty("LINK_TRACK_IGNORE_PATH");

		apiAdminManager = new AdminAdtechApiManager(envPropertyFile, envPropertyFile);
		adminAdaptor = new AdminAdaptorImpl(envPropertyFile);

		templateIds = smsDataPropertyFile.getProperty("TEMPLATE_IDS");
		headerIds = smsDataPropertyFile.getProperty("HEADER_IDS");
		templatesWithSameHeader = smsDataPropertyFile.getProperty("TEMPLATE_ID_WITH_SAME_HEADER");
		revisePanPath= smsDataPropertyFile.getProperty("REVISE_PAN_PATHNAME");
	}

	@BeforeMethod
	public void createUserCompany() {
		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		Response response = onboard.fetchUserByUuid(orderGlobalVariables.getOnboardingServerInitials(), orderGlobalVariables.getUserUUid(),
				"KYC,ORG", uniqueIdentifier);
		userCompanyResponse = commonApi.convertFromJson(response.asString(), UserCompanyResponseVo.class);

		FetchIndustryDTO industry = smsadaptor.fetchIndustry(uniqueIdentifier);

		industriesValues = industry.getIndustries().stream().map(x -> x.getDisplayName()).collect(Collectors.toList());
		industriesKeys = industry.getIndustries().stream().map(x -> x.getKey()).collect(Collectors.toList());

	}

	 @Test(priority=1)
	public void campaignListByPageLimitAndSort() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 1, 2, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 2, 2, false, null);
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=2)
	public void dltListByPageLimitAndSort() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus, null,
				SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 1, 2, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 2, 2, false, null);
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);

	}


	 @Test(priority=3)
	public void campaignListWithZeroPageNo() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, false, null);
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=4)
	public void dltListWithMaxPageNo() {
		List<JSONObject> dltList = adminMongo.getAdminDLTListwithStatus(dltCampaignStatus);
		int size = dltList.size();

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus, null,
				SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, size + 1, 40, 200);
		AdminOrderListResponseDTO actual = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		if (actual.getTotalRecordsCount() != 0) {
			ReportHelper.logValidationFailure("Records Count", "0", String.valueOf(actual.getTotalRecordsCount()),
					"incorrect Count");
			Assert.assertTrue(false);
		}

	}

	 @Test(priority=5)
	public void campaignListWithZeroLimit() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.ASC, AdminOrderSortByKey.createdAt);
		adminAdaptor.fetchorderList(adminreq, 1, 0, 400);

	}

	 @Test(priority=6)
	public void campaignListWithNullLimit() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.ASC, AdminOrderSortByKey.createdAt);
		adminAdaptor.fetchorderList(adminreq, null, 10, 400);

	}

	 @Test(priority=7)
	public void campaignListWithNullOffset() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.ASC, AdminOrderSortByKey.createdAt);
		adminAdaptor.fetchorderList(adminreq, 2, null, 400);

	}

	 @Test(priority=8)
	public void campaignListByPageLimitAndSortDesc() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				null, SortDirection.DESC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 2, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 2, false, null);
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=9)
	public void getCampaignSearchWithTitle() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				title, SortDirection.DESC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);
		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true, "title");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);

	}

	 @Test(priority=10)
	public void getDLTSearchWithCompany() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus,
				userCompanyResponse.getCompany().getName(), SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);
		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true,
				"companyDetails.companyName");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);

	}

	 @Test(priority=11)
	public void getCampaignByPartialTemplateSearch() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.CAMPAIGN, dltCampaignStatus,
				"OT", SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);
		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true,
				"templateSmsContent");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);

	}

	 @Test(priority=12)
	public void getDLTBySearchWithEmail() {
		 
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus,
				userCompanyResponse.getUser().getEmail(), SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true, "profile.email");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=13)
	public void getDLTByPartialSearchWithName() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus,
				userCompanyResponse.getUser().getFirstName(), SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true,
				"profile.contactName");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=14)
	public void getDLTByIncorrectSearch() {
		AdminOrderListDTO adminreq = apiAdminManager.orderListRequestObj(AdminOrderListKey.DLT, dltCampaignStatus,
				"xyhghsdgfh", SortDirection.ASC, AdminOrderSortByKey.createdAt);
		Response response = adminAdaptor.fetchorderList(adminreq, 0, 10, 200);

		AdminOrderListResponseDTO actualList = commonApi.convertFromJsonwithObjectMapperwithclass(response.asString(),
				AdminOrderListResponseDTO.class);
		AdminOrderListResponseDTO expectedList = apiAdminManager.getAdminList(adminreq, 0, 10, true, "xyhghsdgfh");
		commonApi.compareObjectswithObjectMapper(expectedList, actualList, null);
	}

	 @Test(priority=15)
	public void verifyTagPeid() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		AdminTagPeidDTO response = adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 200, false);

		if (response.getPartnerSMSProfileDto().getDltInfo().getPeIdTaggedDate() == null
				|| !response.getPartnerSMSProfileDto().getDltInfo().getPeId().equals(peId)) {
			ReportHelper.logValidationFailure("Peid Or Tagged Date is missing", peId,
					response.getPartnerSMSProfileDto().getDltInfo().getPeId() + " "
							+ String.valueOf(response.getPartnerSMSProfileDto().getDltInfo().getPeIdTaggedDate()),
					"incorrect Peid or Peid date is null ");
			Assert.assertTrue(false);

		}

	}

	 @Test(priority=16)
	public void verifyTagPeidInCampaign() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, null, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 200, false);
		FetchCampaignDTO fetchCampaign = smsadaptor.fetchCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), campaign.getCampaign().getId(), uniqueIdentifier);

		if (!fetchCampaign.getDlt().getPeId().equals(peId))

		{
			ReportHelper.logValidationFailure("Peid  missing", peId, fetchCampaign.getDlt().getPeId(),
					"incorrect Peid or is null ");
			Assert.assertTrue(false);

		}

	}

	 @Test(priority=17)
	public void verifyTagPeidWithDraftDLT() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 404, false);

	}

	 @Test(priority=18)
	public void verifyTagPeidForDLTVerifiedStatus() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 404, false);

	}

	 @Test(priority=19)
	public void verifyFetchDLTDetailswithPan() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.SUBMITTED,true, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), false, null, false);
		AdminPartnerDetailsDTO expectedDltDetails = apiAdminManager.getAdminDltDetails(orderGlobalVariables,
				expectedData, actualdltDetails.getSubmissionDate(), false);

		commonApi.compareObjectswithObjectMapper(expectedDltDetails, actualdltDetails, dltJsonIgnore);

	}

	
	 @Test(priority=20)
	public void validateFetchDLTWithPeidCampaign() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				false, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), true, TemplateStatus.APPROVED, false);
		AdminPartnerDetailsDTO expectedDltDetails = apiAdminManager.getAdminDltDetails(orderGlobalVariables,
				expectedData, actualdltDetails.getSubmissionDate(), true);

		commonApi.compareObjectswithObjectMapper(expectedDltDetails, actualdltDetails, dltJsonIgnore);

	}

	 @Test(priority=21)
	public void verifyNotifyUser() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 200, false);

		Response response = adminAdaptor.notifyUser(orderGlobalVariables.getCompanyUuid());
		if (response.getStatusCode() != 200) {
			ReportHelper.reporterLogging(true, String.valueOf(response.getStatusCode()));
			Assert.assertTrue(false);

		}

	}

	// bug raised
	 @Test(priority=22)
	public void verifyNotifyUserWithoutPeid() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);
		Response response = adminAdaptor.notifyUser(orderGlobalVariables.getCompanyUuid());
		if (response.getStatusCode() != 400) {
			ReportHelper.reporterLogging(false, String.valueOf(response.getStatusCode()) + "Getting response code");
			Assert.assertTrue(false);

		}
	}

	 @Test(priority=23)
	public void verifyNotifyUserWithCampaign() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);
		Response response = adminAdaptor.notifyUser(orderGlobalVariables.getCompanyUuid());
		if (response.getStatusCode() != 400) {
			ReportHelper.reporterLogging(true, String.valueOf(response.getStatusCode()));
			Assert.assertTrue(false);

		}
	}


	 @Test(priority=24)
	public void verifyAdminApproveDLTwithPan() {

		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		adminAdaptor.tagPeid(peId, orderGlobalVariables.getCompanyUuid(), 200, true);
		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId, templateId, template, 200);

		// validate DLT
		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				true, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), false, TemplateStatus.APPROVED, true);
		AdminPartnerDetailsDTO expectedDltDetails = apiAdminManager.getAdminDltDetails(orderGlobalVariables,
				expectedData, actualdltDetails.getSubmissionDate(), false);
		commonApi.compareObjectswithObjectMapper(expectedDltDetails, actualdltDetails, dltJsonIgnore);

		// Validate Campaign
		FetchCampaignDTO fetchCampaign = smsadaptor.fetchCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), campaign.getCampaign().getId(), uniqueIdentifier);
		FetchCampaignDTO expectedFetchCampaign = apiManger.expectedCampaignResponse(campaign.getCampaign().getId(),
				budget, industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, templateId, peId,
				headerId, CampaignStatus.APPROVED, fetchCampaign.getCampaign().getCampaignDuration().getStartDate(),
				null, 0, null, template);
		commonApi.compareObjectswithObjectMapper(expectedFetchCampaign, fetchCampaign, null);

	}

	 @Test(priority=25)
	public void verifyAdminApproveDLTwithPeid() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId, templateId, template, 200);

		// Validate DLT
		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				false, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), true, TemplateStatus.APPROVED, false);
		AdminPartnerDetailsDTO expectedDltDetails = apiAdminManager.getAdminDltDetails(orderGlobalVariables,
				expectedData, actualdltDetails.getSubmissionDate(), false);
		commonApi.compareObjectswithObjectMapper(expectedDltDetails, actualdltDetails, dltJsonIgnore);

		// Validate Campaign
		FetchCampaignDTO fetchCampaign = smsadaptor.fetchCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), campaign.getCampaign().getId(), uniqueIdentifier);
		FetchCampaignDTO expectedFetchCampaign = apiManger.expectedCampaignResponse(campaign.getCampaign().getId(),
				budget, industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, templateId, peId,
				headerId, CampaignStatus.APPROVED, fetchCampaign.getCampaign().getCampaignDuration().getStartDate(),
				null, 0, null, template);
		commonApi.compareObjectswithObjectMapper(expectedFetchCampaign, fetchCampaign, null);

	}

	 @Test(priority=26)
	public void verifyAdminApproveNewTemplate() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId,
				templatesWithSameHeader.split(",")[0], template, 200);

		FetchDLTInfoDTO actualdltInfo = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		
		

		if (!actualdltInfo.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().get(1).getId()
				.equals(templatesWithSameHeader.split(",")[0])) {
			ReportHelper.logValidationFailure("HeaderId", "ART",
					actualdltInfo.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().get(1).getId(),
					"incorrect status ");
			Assert.assertTrue(false);

		}

	}

	 @Test(priority=27)
	public void verifyAdminApprovePanOrderWithNewTemplate() {

		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, templateURL);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		// approve campaign with template and peid
		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId,
				templatesWithSameHeader.split(",")[0], template, 200);

		SubmitCampaignDTO createObjSecond = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi,
				radius, audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaignSecond = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObjSecond, null, true, 200, uniqueIdentifier);

		adminAdaptor.approveCampaign(campaignSecond.getCampaign().getId(), peId, headerId,
				templatesWithSameHeader.split(",")[1], template, 200);

		FetchDLTInfoDTO actualdltInfo = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		if (!(actualdltInfo.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size() == 2)) {
			ReportHelper.logValidationFailure("Campaign Header Size", "2",
					String.valueOf(actualdltInfo.getDltInfo().getHeaderInfo().size()), "incorrect size ");
			Assert.assertTrue(false);

		}

	}

	 @Test(priority=28)
	public void verifyAdminApproveDuplicateHeader() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId, templateId, template, 400);
	}

	 @Test(priority=29)
	public void verifyDownloadFile() {
		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		adminAdaptor.downloadFile(actualdltDetails.getRegistrationDetails().getGstDocument().getDocumentKey(),
				DocumentType.GST, 200);

	}

	
	 @Test(priority=30)
	public void verifyDownloadFileNegative() {
		adminAdaptor.downloadFile("Invalid file", DocumentType.GST, 404);
	}

	 @Test(priority=31)

	public void verifySubmissionNullDateinFetchDLT() {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		AdminPartnerDetailsDTO actualdltDetails = adminAdaptor.fetchDltAdmin(orderGlobalVariables.getCompanyUuid(),
				200);
		if (actualdltDetails.getSubmissionDate() != null) {
			ReportHelper.logValidationFailure("Submission Date", "Null",
					String.valueOf(actualdltDetails.getSubmissionDate()), "submission date should be null ");
			Assert.assertTrue(false);

		}
	}

	@Test(priority=32)
	public void reviseDLTandUpdateDoc() {

		apiManger.registerDLtwithPan(orderGlobalVariables, true, userCompanyResponse, uniqueIdentifier);

		adminAdaptor.reviseDlt(orderGlobalVariables.getCompanyUuid(), "Test Revise DLT", 200);
		PartnerSMSProfileDTO partnerProfile = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		PartnerSMSProfileDTO expectedProfile = apiManger.expectedDLTMongo(orderGlobalVariables,DLTProfileStatus.REVISED, false, OrganisationType.GOVERNMENT,userCompanyResponse.getCompany().getTradeName(), userCompanyResponse.getCompany().getPanNumber(), false,partnerProfile.getContract().getDocumentKey(), null, partnerProfile.getCreatedAt(),
				partnerProfile.getUpdatedAt(),partnerProfile.getContract().getCreatedAt(),partnerProfile.getSubmissionDate() );
		commonApi.compareObjectswithObjectMapper(expectedProfile, partnerProfile, dltJsonIgnore);

		// update DLT
		smsadaptor.reviseDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, revisePanPath, OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		PartnerSMSProfileDTO profile = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());
		if (!profile.getCompanyDetails().getProofEntityDocument().getOriginalName().equals("adtechPan.jpg")) {
			ReportHelper.logValidationFailure("Pan Name", "adtechPan.jpg",	profile.getCompanyDetails().getProofEntityDocument().getOriginalName(),	"incorrect pan path name  ");
			Assert.assertTrue(false);
		}
	}

	 @Test(priority=33)
	public void reviseandUpdateCampaign() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		adminAdaptor.reviseCampaign(campaign.getCampaign().getId(), 200);
		FetchCampaignDTO fetchCampaign = smsadaptor.fetchCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), campaign.getCampaign().getId(), uniqueIdentifier);
		if (fetchCampaign.getCampaign().getStatus().name() != "DRAFT") {
			ReportHelper.logValidationFailure("Campaign Status", "DRAFT",
					fetchCampaign.getCampaign().getStatus().name(), "incorrect campaign status  ");
			Assert.assertTrue(false);
		}

	}

	 @Test(priority=34)
	public void reviseDLTVerified() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		adminAdaptor.reviseDlt(orderGlobalVariables.getCompanyUuid(), "Test Revise DLT", 400);
	}

	 @Test(priority=35)
	public void reviseDLTDraft() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);
		adminAdaptor.reviseDlt(orderGlobalVariables.getCompanyUuid(), "Test Revise DLT", 400);
	}
//bug raised
	 @Test(priority=36)
	public void reviseCampaignDraft() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, false, 200, uniqueIdentifier);
		adminAdaptor.reviseCampaign(campaign.getCampaign().getId(), 400);
	}

	
	 @Test(priority=37)
	public void revisecampaignApproved() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date, null, template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		adminAdaptor.approveCampaign(campaign.getCampaign().getId(), peId, headerId, templatesWithSameHeader.split(",")[0], template, 200);
		adminAdaptor.reviseCampaign(campaign.getCampaign().getId(), 400);
	}



}
