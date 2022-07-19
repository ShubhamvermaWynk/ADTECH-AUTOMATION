package com.airtel.RestAssured.Adtech.Tests.sms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.airtel.adtech.adaptor.sms.SmsAdaptor;
import com.airtel.adtech.adaptor.sms.SmsAdaptorImpl;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserAdaptor;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserIAdaptormpl;
import com.airtel.adtech.constants.enums.AudienceStatus;
import com.airtel.adtech.constants.enums.CampaignExecutionStatus;
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.TemplateStatus;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.dto.response.sms.FetchTargetandBudgetDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderListDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO.SMSHeaderDto;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;

import com.airtel.helper.report.ReportHelper;

import com.airtel.teams.common.CommonApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;

import io.restassured.response.Response;

public class CampaignTest extends ReportHelper {

	CommonApi commonApi = new CommonApi();
	OnboardUserAdaptor onboard;


	String uniqueIdentifier;
	Connection billingCon;
	SmsAdtechApiManager apiManger;
	SmsAdaptor smsadaptor;
	SmsAdMongo smsMongo;
	String dltJsonIgnore;

	Properties smsDataPropertyFile;
	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;

	String peId, headerId, templateId, template,templateURL;

	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	List<String> industriesKeys;
	List<String> industriesValues;
	
	static Instant date;

	String serverInitials;
	String templateIds,headerIds;
	String templatesWithSameHeader;
	OnboardApiManager onboardApiManager;
	OrderGlobalVariables orderGlobalVariables;
	UserCompanyResponseVo userCompanyResponse;

	@Parameters({ "environment" })
	@BeforeClass
	public void setEnvironment(@Optional("qa_1") String environment) {

		Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
		onboard= new OnboardUserIAdaptormpl();
		onboardApiManager= new OnboardApiManager(envPropertyFile);
		smsDataPropertyFile = commonApi.getTestDataConfigPropertyObject("adtechSms");
		dltJsonIgnore=smsDataPropertyFile.getProperty("DLT_JSON_IGNORE");
		serverInitials = envPropertyFile.getProperty("USERS_URL");
		orderDataPropertyFile = commonApi.getTestDataConfigPropertyObject("order");
		onboardingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("onboarding");
		paymentDataPropertyFile = commonApi.getTestDataConfigPropertyObject("payment");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);
		
		uniqueIdentifier = CommonApi.uniqueidentifier();

		smsadaptor = new SmsAdaptorImpl(envPropertyFile);
		smsMongo = new SmsAdMongoImpl(envPropertyFile);
		apiManger = new SmsAdtechApiManager(envPropertyFile, smsDataPropertyFile);

		peId = smsDataPropertyFile.getProperty("PEID");
		headerId = smsDataPropertyFile.getProperty("HEADER_ID");
		templateId = smsDataPropertyFile.getProperty("TEMPLATEID");
		template = smsDataPropertyFile.getProperty("TEMPLATE");
		templateURL=smsDataPropertyFile.getProperty("TEMPLATE_WITH_URL");
	

		lat = Double.valueOf(smsDataPropertyFile.getProperty("LAT"));
		longi = Double.valueOf(smsDataPropertyFile.getProperty("LONGI"));
		radius = Long.valueOf(smsDataPropertyFile.getProperty("RADIUS"));
		audienceReachCount = Long.valueOf(smsDataPropertyFile.getProperty("REACH_COUNT"));
		budget = new BigDecimal(smsDataPropertyFile.getProperty("BUDGET"));

		date = Instant.now().plus(2, ChronoUnit.DAYS);
		
		templateIds=smsDataPropertyFile.getProperty("TEMPLATE_IDS");
		headerIds=smsDataPropertyFile.getProperty("HEADER_IDS");
		
		templatesWithSameHeader= smsDataPropertyFile.getProperty("TEMPLATE_ID_WITH_SAME_HEADER");
		onboard= new OnboardUserIAdaptormpl();
	}
	
	

	@BeforeMethod
	public void createUserCompany() {
		
		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		Response response = onboard.fetchUserByUuid(serverInitials, orderGlobalVariables.getUserUUid(),
				"KYC,ORG", uniqueIdentifier);
		userCompanyResponse = commonApi.convertFromJson(response.asString(), UserCompanyResponseVo.class);

		FetchIndustryDTO industry = smsadaptor.fetchIndustry(uniqueIdentifier);

		industriesValues = industry.getIndustries().stream().map(x -> x.getDisplayName()).collect(Collectors.toList());
		industriesKeys = industry.getIndustries().stream().map(x -> x.getKey()).collect(Collectors.toList());
		
	}

	@Test(priority = 1)
	public void submitCampaignandValidatewithPeid() {

	smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		Instant currentDate=Instant.now();
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null,currentDate ,null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		FetchCampaignDTO expectedObj = apiManger.expectedCampaignResponse(responseCampaign.getCampaign().getId(),
				budget, industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, templateId, peId,
				headerId, CampaignStatus.SUBMITTED, currentDate,null, 0, null, template);
		commonApi.compareObjectswithObjectMapper(expectedObj, responseCampaign, null);

		// Call admin api and approve the campaign
		smsadaptor.approveCampaignbyId(responseCampaign.getCampaign().getId(), 200, uniqueIdentifier);

		// GetMongo data

		CamapignMongoDTO actualCampaignMongo = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		CamapignMongoDTO expectedCampaignMongo = apiManger.expectedCampaignMongo(actualCampaignMongo.getAudience(),actualCampaignMongo.getCampaignId(), orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), CampaignStatus.APPROVED, budget,actualCampaignMongo.getCampaignDuration().getStartDate(), actualCampaignMongo.getCreatedAt(),actualCampaignMongo.getUpdatedAt(), 0, null,template, null);
		commonApi.compareObjectswithObjectMapper(expectedCampaignMongo, actualCampaignMongo, null);

		AudienceMongoDTO audience = smsMongo.getAudienceById(actualCampaignMongo.getAudience());
		


		if (!(audience.getStatus().equals(AudienceStatus.FILE_CREATION_COMPLETED)   ||  audience.getStatus().equals(AudienceStatus.FILE_CREATION_IN_PROGRESS) || audience.getStatus().equals(AudienceStatus.APPROVED) )) {
			ReportHelper.logValidationFailure("Audience Status", "FILE_CREATION_IN_PROGRESS",
					audience.getStatus().toString(), "incorrect status ");
			Assert.assertTrue(false);
		}
		
		

	}

	@Test(priority = 2)
	public void submitCampaignandValidatewithPan() throws InterruptedException {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, null, null, null, date,null,template);
		FetchCampaignDTO actualObj = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		FetchCampaignDTO expectedObj = apiManger.expectedCampaignResponse(actualObj.getCampaign().getId(), budget,industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, null, null, null,	CampaignStatus.SUBMITTED, date,null, 0, null, template);
		 commonApi.compareObjectswithObjectMapper(expectedObj, actualObj, null);

		// Call admin api and approve the campaign
		smsadaptor.approveDLT(actualObj.getCampaign().getId(), headerId, templateId, template, peId,orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		// GetMongo data
		CamapignMongoDTO actualCampaignMongo = smsMongo.getCampaignByCampaignId(actualObj.getCampaign().getId());
		CamapignMongoDTO expectedCampaignMongo = apiManger.expectedCampaignMongo(actualCampaignMongo.getAudience(),actualCampaignMongo.getCampaignId(), orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), CampaignStatus.APPROVED, budget,	actualCampaignMongo.getCampaignDuration().getStartDate(), actualCampaignMongo.getCreatedAt(),actualCampaignMongo.getUpdatedAt(), 0, null, template, null);
		commonApi.compareObjectswithObjectMapper(expectedCampaignMongo, actualCampaignMongo, null);

		Thread.sleep(4000);
		AudienceMongoDTO audience = smsMongo.getAudienceById(actualCampaignMongo.getAudience());

		if (!(audience.getStatus().equals(AudienceStatus.FILE_CREATION_COMPLETED)   ||  audience.getStatus().equals(AudienceStatus.FILE_CREATION_IN_PROGRESS) || audience.getStatus().equals(AudienceStatus.APPROVED) ))
		{
			ReportHelper.logValidationFailure("Audience Status", "FILE_CREATION_IN_PROGRESS  FILE_CREATION_COMPLETED",audience.getStatus().toString(), "incorrect status ");
			Assert.assertTrue(false);
		}
		SubmitCampaignDTO secondCampaign = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),secondCampaign, null, true, 200, uniqueIdentifier);

	}
	

	@Test(priority = 3)
	public void registerNewTemplatewithDLT() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		

		SubmitCampaignDTO createObjSecond = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi,radius, audienceReachCount, null, peId, headerId, null, date,null,template);
		FetchCampaignDTO secondCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObjSecond, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaign(secondCampaign.getCampaign().getId(), templatesWithSameHeader.split(",")[0], template,200, uniqueIdentifier);

		CamapignMongoDTO actualCampaignMongo = smsMongo.getCampaignByCampaignId(secondCampaign.getCampaign().getId());

		if (!(actualCampaignMongo.getStatus().equals(CampaignStatus.APPROVED))) {

			ReportHelper.logValidationFailure("Campaign Status", "APPROVED",
					String.valueOf(actualCampaignMongo.getStatus()), "incorrect status ");
			Assert.assertTrue(false);

		}
		// verify new template in DLt Api.
		FetchDLTInfoDTO fetchDLT = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), uniqueIdentifier);

		if (!(fetchDLT.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size() == 2)) {

			ReportHelper.logValidationFailure("Templates Size", "2",
					String.valueOf(fetchDLT.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size()),
					"incorrect size ");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 4)
	public void fetchandSubmitCampaign() {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, null, null, null, date,null,template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		// approve campaign with template and peid
		smsadaptor.approveDLT(responseCampaign.getCampaign().getId(), headerId, templateId, template, peId,
				orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		FetchCampaignDTO fetchCampaign = smsadaptor.fetchCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), responseCampaign.getCampaign().getId(), uniqueIdentifier);
		FetchCampaignDTO expectedFetchCampaign = apiManger.expectedCampaignResponse(
				responseCampaign.getCampaign().getId(), budget, industriesKeys, industriesValues, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, CampaignStatus.APPROVED,
				fetchCampaign.getCampaign().getCampaignDuration().getStartDate(),null, 0, null, template);
		commonApi.compareObjectswithObjectMapper(expectedFetchCampaign, fetchCampaign, null);
	}

	@Test(priority = 5)

	public void updateExistingCampaign() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, false, 200, uniqueIdentifier);

		// update existing order
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		if (responseCampaign.getCampaign().getStatus() != CampaignStatus.SUBMITTED) {
			ReportHelper.logValidationFailure("Campaign Status", "SUBMITTED",
					String.valueOf(responseCampaign.getCampaign().getStatus()), "incorrect status ");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 6)
	public void multipleCampaignWithDLTapproved() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		// Call admin api and approve the campaign
		smsadaptor.approveCampaignbyId(responseCampaign.getCampaign().getId(), 200, uniqueIdentifier);

		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		List<CamapignMongoDTO> campaigns = smsMongo.getCamapignByComapnyId(orderGlobalVariables.getCompanyUuid());

		if (campaigns.size() != 4) {
			ReportHelper.logValidationFailure("Multiplke campaign size", "4", String.valueOf(campaigns.size()),
					"incorrect status ");
			Assert.assertTrue(false);
		}

	}

	@Test(priority = 7)
	public void multiCampaignswithSubmittedStatus() {

		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

	}

	@Test(priority = 8)
	public void createOrderWithoutDLT() {


		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, null, null, null, date,null,template);
		 smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 404, uniqueIdentifier);
//		CamapignMongoDTO actualCampaignMongo = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
//		if(!actualCampaignMongo.getStatus().name().equals("REJECTED"))
//		{
//			ReportHelper.logValidationFailure("Campaign Status", "REJECTED", actualCampaignMongo.getStatus().name(),"incorrect status ");
//			Assert.assertTrue(false);
//			
//		}

	}

	
	@Test(priority = 9)
	public void orderwithInvalidIndustry() {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, Arrays.asList("DUMMY"), lat, longi,
				radius, audienceReachCount, null, null, null, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 10)
	public void createCamapignwithCurrentDate() {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, null, null, null, null, Instant.now(),null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 11)
	public void createCamapignwithIncorrectTemplateId() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,
				audienceReachCount, "1234567", peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 12)
	public void calculateBudgetandcreateOrder() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		FetchTargetandBudgetDTO target = smsadaptor.submitBudgetandTarget(lat, longi, radius, industriesKeys.get(0),
				200, orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, target.getBudget(), industriesKeys, lat, longi,
				radius, target.getAudienceReachCount(), templateId, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,
				null, true, 200, uniqueIdentifier);

	}

	@Test(priority = 13)
	public void orderWithInvalidIndustryinBudgetApi() {
		smsadaptor.submitBudgetandTarget(lat, longi, radius, "dummyIndustry", 400,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);

	}

	

	@Test(priority = 14)
	public void verifySchedulerwithCompletedStatus() throws JsonProcessingException, InterruptedException {
		
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
		Thread.sleep(1000);
		CamapignMongoDTO campaignMongoUpdated = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoUpdated.getStatus().equals(CampaignStatus.EXECUTION_SUBMITTED)))
		{
			ReportHelper.logValidationFailure("Campaign status", "EXECUTION_SUBMITTED",campaignMongoUpdated.getStatus().name(), "incorrect status ");
			//Assert.assertTrue(false);

		}

		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS, null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,null, null);

		Thread.sleep(2000);
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.COMPLETED))) {
			ReportHelper.logValidationFailure("Campaign status", "COMPLETED", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}

	}
	
	
	@Test(priority = 15)
	public void verifySchedulerwithFailedStatus() throws JsonProcessingException, InterruptedException {
		
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
			
		

		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.ERROR,null, null);
		
		Thread.sleep(2000);
		
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.FAILED))) {
			ReportHelper.logValidationFailure("Campaign status", "FAILED", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}

	}
	//bug
	@Test(priority = 16)
	public void verifySchedulerwithDummyEvent() throws JsonProcessingException, InterruptedException {
		
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
			Thread.sleep(1000);
	
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.DUMMY_EVENT,null, null);

		Thread.sleep(2000);
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.PUSH_IN_PROCESS))) {
			ReportHelper.logValidationFailure("Campaign status", "PUSH_IN_PROCESS", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}

	}
	
	
	@Test(priority = 17)
	public void verifySchedulerwithFileCreationEvent() throws JsonProcessingException {
		
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		
		
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.FILE_CREATION_IN_PROGRESS,null, null);

		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.APPROVED) || campaignMongoCompleted.getStatus().equals(CampaignStatus.PUSH_IN_PROCESS) ||  campaignMongoCompleted.getStatus().equals(CampaignStatus.COMPLETED))) {
			ReportHelper.logValidationFailure("Campaign status", "APPROVED", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 18)
	public void fetchOrderListandValidate() {
		
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		 FetchCampaignDTO campaignSecond= smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		 smsadaptor.approveCampaignbyId(campaignSecond.getCampaign().getId(), 200,uniqueIdentifier);

		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 200, uniqueIdentifier);
		 smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 200, uniqueIdentifier);

		SubmitCampaignDTO obj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), obj, null,false, 200, uniqueIdentifier);
		 smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), obj,null, false, 200, uniqueIdentifier);

		SmsOrderListDTO actualorderList = smsadaptor.fetchOrderList(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), uniqueIdentifier);

		List<CamapignMongoDTO> orderListMongo = smsMongo.getCamapignByComapnyId(orderGlobalVariables.getCompanyUuid());
		SmsOrderListDTO expectedorderList = apiManger.expectedOrderList(orderListMongo);

		commonApi.compareObjectswithObjectMapper(expectedorderList, actualorderList, null);
		

	}
	
	
	
	@Test(priority = 19)
	public void createOrderWithMultipleHeaders() throws JsonProcessingException, InterruptedException
	{
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerId, templateURL, templateId, 200, uniqueIdentifier);
		
		for(int i=0; i<3; i++)
		{smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerIds.split(",")[i], templateURL, templateIds.split(",")[i], 200, uniqueIdentifier);}
		
		
		FetchDLTInfoDTO actualdltInfo=smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expecteddltInfo= apiManger.expectedFetchDLTWithMultipleHeaders(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED, false, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(), userCompanyResponse.getCompany().getPanNumber(), true, TemplateStatus.APPROVED,false, "VERIFIED");
		
		commonApi.compareObjectswithObjectMapper(expecteddltInfo, actualdltInfo, null);
		
		//create campaign and approve.
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateIds.split(",")[0], peId, headerIds.split(",")[0], null, date,null,templateURL);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		
		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS, null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,null, null);

		Thread.sleep(2000);
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.COMPLETED))) {
			ReportHelper.logValidationFailure("Campaign status", "COMPLETED", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}
		
	}
	

	@Test(priority = 20)
	public void createOrderWithNewTemplate()
	{
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerIds.split(",")[0] , templateURL, templateIds.split(",")[0], 200, uniqueIdentifier);
		
		SubmitCampaignDTO createObjSecond = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi,radius, audienceReachCount, null, peId, headerId, null, date,null,template);
		FetchCampaignDTO secondCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObjSecond, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaign(secondCampaign.getCampaign().getId(), templatesWithSameHeader.split(",")[0], template, 200, uniqueIdentifier);
		
		FetchDLTInfoDTO actualdltInfo=smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		
		if(!actualdltInfo.getDltInfo().getHeaderInfo().get(1).getTemplateMeta().get(0).getId().equals(templateIds.split(",")[0]))
		{	
			ReportHelper.logValidationFailure("HeaderId", "ART", actualdltInfo.getDltInfo().getHeaderInfo().get(1).getTemplateMeta().get(1).getId(),"incorrect status ");
			Assert.assertTrue(false);
			
		}
	}
	

	@Test(priority = 21)
	public void createOrderWithmultipleTemplatewithSingleHeader()
	{
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerIds.split(",")[1] , templateURL, templateIds.split(",")[1], 200, uniqueIdentifier);
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerIds.split(",")[1] , templateURL, "1007071744272700519", 200, uniqueIdentifier);
		
		FetchDLTInfoDTO actualdltInfo=smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		
		if(!(actualdltInfo.getDltInfo().getHeaderInfo().get(1).getTemplateMeta().size()==2))
		{
			ReportHelper.logValidationFailure("HeaderId", "2", String.valueOf(actualdltInfo.getDltInfo().getHeaderInfo().get(1).getTemplateMeta().size()),"incorrect status ");
			Assert.assertTrue(false);
			
		}
	}
	
	@Test(priority = 22)
	public void panOrderWithMultipleHeaders() throws JsonProcessingException, InterruptedException
	{
		
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		
		
		SubmitCampaignDTO createObj= apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, null, null, null, date,null ,templateURL);
		FetchCampaignDTO responseCampaign= smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj, null,true, 200, uniqueIdentifier);

		// approve campaign with template and peid
		smsadaptor.approveDLT(responseCampaign.getCampaign().getId(), headerId, templateId, templateURL, peId,
				orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);
		
		for(int i=0; i<3; i++)
		{smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerIds.split(",")[i], templateURL, templateIds.split(",")[i], 200, uniqueIdentifier);}
		
		
		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		
		FetchDLTInfoDTO actualdltInfo=smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expecteddltInfo= apiManger.expectedFetchDLTWithMultipleHeaders(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED, true, OrganisationType.GOVERNMENT,userCompanyResponse.getCompany().getTradeName(), userCompanyResponse.getCompany().getPanNumber(), false, TemplateStatus.APPROVED,true, "VERIFIED");
		
		commonApi.compareObjectswithObjectMapper(expecteddltInfo, actualdltInfo, dltJsonIgnore);
		
		smsMongo.updateStartDateinCampaign(responseCampaign.getCampaign().getId(),campaignMongo.getCreatedAt());
		
		apiManger.pushStatusEventinKafka(responseCampaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS, null, null);
		apiManger.pushStatusEventinKafka(responseCampaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,null, null);

		Thread.sleep(2000);
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.COMPLETED))) {
			ReportHelper.logValidationFailure("Campaign status", "COMPLETED", campaignMongoCompleted.getStatus().name(),
					"incorrect status ");
			Assert.assertTrue(false);

		}
		
	}
	
	@Test(priority = 23)
	public void panOrderWithNewTemplate()
	{
		
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,uniqueIdentifier);
		
		
		SubmitCampaignDTO createObj= apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, null, null, null, date,null ,templateURL);
		FetchCampaignDTO responseCampaign= smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj, null,true, 200, uniqueIdentifier);

		// approve campaign with template and peid
		smsadaptor.approveDLT(responseCampaign.getCampaign().getId(), headerId, templateId, templateURL, peId,
				orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);
		
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,headerId , template, templateId, 200, uniqueIdentifier);
		
		SubmitCampaignDTO createObjSecond = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi,radius, audienceReachCount, null, peId, headerId, null, date,null,template);
		FetchCampaignDTO secondCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObjSecond, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaign(secondCampaign.getCampaign().getId(), templatesWithSameHeader.split(",")[0], template, 200, uniqueIdentifier);
		
		FetchDLTInfoDTO actualdltInfo=smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		if(!(actualdltInfo.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size()==2))
		{
			ReportHelper.logValidationFailure("Campaign Header Size", "2", String.valueOf(actualdltInfo.getDltInfo().getHeaderInfo().size()),"incorrect size ");
			Assert.assertTrue(false);
			
		}
		
		
		
	}
	
	@Test(priority = 24)
	public void registerHeaderWithoutDLT()
	{
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerIds.split(",")[0], templateURL,templateIds.split(",")[0] , 400, uniqueIdentifier);
		
	}
	@Test(priority = 25)
	public void cretaOrderWithUnregisteredHeader()
	{
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateIds.split(",")[0], peId, headerIds.split(",")[0], null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 400, uniqueIdentifier);
		
	}
	@Test(priority = 26)
	public void verifyRandomTemplatewithHeader()
	{
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);
		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId, headerIds.split(",")[0], templateURL,templateIds.split(",")[0] , 200, uniqueIdentifier);
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateIds.split(",")[1], peId, headerIds.split(",")[0], null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 400, uniqueIdentifier);
		
		
	}
	
	
	
	
	
	
	
	
}
