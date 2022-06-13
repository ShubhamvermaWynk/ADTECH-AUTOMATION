package com.airtel.RestAssured.Adtech.Tests.sms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.testng.Assert;
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
import com.airtel.adtech.constants.enums.AudienceStatus;
import com.airtel.adtech.constants.enums.CampaignExecutionStatus;
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.ValidityUnit;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.mongo.GetLinkTrackingMongoDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.dto.response.sms.FetchTargetandBudgetDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderListDTO;
import com.airtel.adtech.entity.CampaignMetaData.SmsLinkDetails;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;

import com.airtel.helper.report.ReportHelper;

import com.airtel.teams.common.CommonApi;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.response.Response;

public class AnalyticsTimeSlotTest extends ReportHelper {

	CommonApi commonApi = new CommonApi();
	String uniqueIdentifier;
	Connection billingCon;
	SmsAdtechApiManager apiManger;
	SmsAdaptor smsadaptor;
	SmsAdMongo smsMongo;

	Properties smsDataPropertyFile;
	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;

	String peId, headerId, templateId, template,templateURL;
	
	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	List<String> industriesKeys;
	List<String> industriesValues;
	Instant date;
	

	String serverInitials;
	String timeSlot = "10:00-11:00";
	String invalidTimeSlot = "9:00-3:00";
	String timeSlotFormat = "10-11";
	String timeSlotAfterNine = "23:00-23:30";
	String tweleveHourtimeSlot = "2:00-4:30";
	String shortCode;
	String linkIgnoreJson;
	AdminAdaptor adminAdaptor;
	
	OnboardApiManager onboardApiManager;
	OrderGlobalVariables orderGlobalVariables;
	UserCompanyResponseVo userCompanyResponse;
	OnboardUserAdaptor onboard;
	

	@Parameters({ "environment" })
	@BeforeClass
	public void setEnvironment(@Optional("qa_1") String environment) {

		Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
		onboard= new OnboardUserIAdaptormpl();
		onboardApiManager= new OnboardApiManager(envPropertyFile);
		smsDataPropertyFile = commonApi.getTestDataConfigPropertyObject("adtechSms");
		serverInitials = envPropertyFile.getProperty("USERS_URL");
		onboardApiManager= new OnboardApiManager(envPropertyFile);
		orderDataPropertyFile = commonApi.getTestDataConfigPropertyObject("order");
		onboardingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("onboarding");
		paymentDataPropertyFile = commonApi.getTestDataConfigPropertyObject("payment");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);
		
		
		uniqueIdentifier = CommonApi.uniqueidentifier();

		adminAdaptor= new AdminAdaptorImpl(envPropertyFile);
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
		shortCode=smsDataPropertyFile.getProperty("SHORT_CODE");
		linkIgnoreJson=smsDataPropertyFile.getProperty("LINK_TRACK_IGNORE_PATH");
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
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, templateURL, templateId, 200, uniqueIdentifier);

	}

	


	@Test(priority = 1)
	public void fetchAnalytics() throws InterruptedException, JsonProcessingException {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, null,templateURL);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date

		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), campaignMongo.getCreatedAt());

		
		GetAnalyticsMongoDTO analyticsActual = smsadaptor.getAnalyticsById(campaign.getCampaign().getId(), 200,
				uniqueIdentifier);

		List<JSONObject> analyticsActualJson=smsMongo.getAnalyticsByIdJson(campaign.getCampaign().getId());
		
		int count=0;
		while(analyticsActualJson.size()==0 && count <=400)
		{
			Thread.sleep(3000);
			
			System.out.println("Going to sleep while waiting for link statics to bve created(sec): 10");
			analyticsActualJson = smsMongo.getAnalyticsByIdJson(campaign.getCampaign().getId());
			
			count=count+10;
		}
		
		
		GetAnalyticsMongoDTO analyticsActualMongo = commonApi.convertFromJsonwithObjectMapperwithclass(analyticsActualJson.get(0).toString(),
				GetAnalyticsMongoDTO.class);
		
		if ((analyticsActualMongo.getDeliveredCount() != 1) || (!analyticsActualMongo.getStatus().equals("SUCCESS"))
				|| (analyticsActualMongo.getTotalSmsReceivedByIq() != 1)
				|| (analyticsActual.getTotalSmsSentToIq() != 1)) {
			ReportHelper.logValidationFailure("Analytics status", "SUCCESS", analyticsActualMongo.getStatus(),
					"incorrect status ");
			Assert.assertTrue(false);

		}
	}
	
	@Test(dependsOnMethods ="fetchAnalytics",priority = 2)
	public void fetchAnalyticsAdmin()
	{
		Response response= adminAdaptor.fetchAnalyticsAdmin(orderGlobalVariables.getCompanyUuid());
		if(response.getStatusCode()!=200)
		{
			ReportHelper.logValidationFailure("Analytics Admin", "200",String.valueOf(response.getStatusCode()), "incorrect Count");
			Assert.assertTrue(false);
		}
		
	}
	

	@Test(priority = 3)
	public void verifyTimeSlotPositiveCase() throws InterruptedException, JsonProcessingException {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, timeSlot,templateURL);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		FetchCampaignDTO expectedObj = apiManger.expectedCampaignResponse(campaign.getCampaign().getId(), budget,industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, templateId, peId, headerId,CampaignStatus.SUBMITTED, date, timeSlot, 21600, ValidityUnit.MINUTES,templateURL);
		commonApi.compareObjectswithObjectMapper(expectedObj, campaign, null);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		// update mongo date to past date
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), campaignMongo.getCreatedAt());

		Thread.sleep(1000);
		CamapignMongoDTO campaignMongoUpdated = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoUpdated.getStatus().equals(CampaignStatus.EXECUTION_SUBMITTED))) {
			ReportHelper.logValidationFailure("Campaign status", "EXECUTION_SUBMITTED",campaignMongoUpdated.getStatus().name(), "incorrect status ");
			// Assert.assertTrue(false);

		}

		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,null, null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,null,null);

		Thread.sleep(3000);
		CamapignMongoDTO campaignMongoCompleted = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if (!(campaignMongoCompleted.getStatus().equals(CampaignStatus.COMPLETED))) {
			ReportHelper.logValidationFailure("Campaign status", "COMPLETED", campaignMongoCompleted.getStatus().name(),"incorrect status ");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 4)
	public void invalidTimeSlot() {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, invalidTimeSlot,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 5)
	public void invalidTimeSlotFormat() {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, timeSlotFormat,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 6)
	public void timeSlotAfterNine() {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, timeSlotAfterNine,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,	null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 7)
	public void timeSlotWithTweleveHourWindow() {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, tweleveHourtimeSlot,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,	null, true, 400, uniqueIdentifier);

	}

	@Test(priority = 8)
	public void outdatedCampaignDuration() throws InterruptedException {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,	audienceReachCount, templateId, peId, headerId, null,Instant.now().minus(2, ChronoUnit.DAYS) , timeSlot,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 400, uniqueIdentifier);


	}

	@Test(priority = 9)
	public void futureDatedCampaignDuration() throws InterruptedException {
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, timeSlot,templateURL);
		FetchCampaignDTO campaign =smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), createObj,null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);

		
		Thread.sleep(1000);
		CamapignMongoDTO campaignMongoUpdated = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		if ((campaignMongoUpdated.getStatus().equals(CampaignStatus.EXECUTION_SUBMITTED))) {
			ReportHelper.logValidationFailure("Campaign status", "EXECUTION_SUBMITTED",campaignMongoUpdated.getStatus().name(), "incorrect status ");
			Assert.assertTrue(false);

		}

	}
	
	@Test(priority = 10)
	public void validateLinkTracking() throws InterruptedException, JsonProcessingException
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, null,templateURL,2,ValidityUnit.MINUTES);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		FetchCampaignDTO expectedObj = apiManger.expectedCampaignResponse(campaign.getCampaign().getId(),budget, industriesKeys, industriesValues, lat, longi, radius, audienceReachCount, templateId, peId,headerId, CampaignStatus.SUBMITTED, date,null, 2, ValidityUnit.MINUTES,templateURL);
		commonApi.compareObjectswithObjectMapper(expectedObj, campaign, null);
		
		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		
		// update mongo date to past date
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), Instant.now().minus(2, ChronoUnit.HOURS));
		
		
		Instant startDate= Instant.now();
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,shortCode,startDate);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,shortCode,startDate);
		
		Thread.sleep(3000);
		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		CamapignMongoDTO expectedCampaignMongo = apiManger.expectedCampaignMongo(campaignMongo.getAudience(),campaignMongo.getCampaignId(), orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), CampaignStatus.COMPLETED, budget,	campaignMongo.getCampaignDuration().getStartDate(), campaignMongo.getCreatedAt(),campaignMongo.getUpdatedAt(), 2, ValidityUnit.MINUTES, templateURL,startDate);
		commonApi.compareObjectswithObjectMapper(expectedCampaignMongo, campaignMongo, null);
		
		
		List<JSONObject> linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId());
		
		int staticCounter=0;
		while(linkMongo.size()==0 && staticCounter<=400) {
			
			Thread.sleep(2000);
			System.out.println(
					"Going to sleep while waiting for link statics to bve created(sec): 10");

			linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId());
			staticCounter = staticCounter + 10;
			
		}
		
		
		
		GetLinkTrackingMongoDTO audienceData = commonApi.convertFromJsonwithObjectMapperwithclass(linkMongo.get(0).toString(),
				GetLinkTrackingMongoDTO.class);
		
		GetLinkTrackingMongoDTO expectedMongo =apiManger.getexpectedLinkMongo(campaign.getCampaign().getId(),  2, ValidityUnit.MINUTES, campaignMongo.getCampaignMetadata().getSmsLinkDetails().getUrlCreationDate());
		
		commonApi.compareObjectswithObjectMapper(expectedMongo, audienceData, linkIgnoreJson);
		
		
		Thread.sleep(6000);
		GetAnalyticsMongoDTO analyticsActual = smsadaptor.getAnalyticsById(campaign.getCampaign().getId(), 200,
				uniqueIdentifier);
		
		if(!(analyticsActual.getLinkClickedCount()>=6))
		{
			ReportHelper.logValidationFailure("Analytics Link Clicked", "6",String.valueOf(analyticsActual.getLinkClickedCount()), "incorrect Value in analytics ");
			Assert.assertTrue(false);
		}
				
		
		
	}
	

	
	@Test(priority = 11)
	public void verifylinkScehdulerforApprovedOrders() throws InterruptedException
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, null,templateURL,2,ValidityUnit.MINUTES);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);


		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		
		// update mongo date to past date
		CamapignMongoDTO campaignMongo = smsMongo.getCampaignByCampaignId(campaign.getCampaign().getId());
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), campaignMongo.getCreatedAt());
		Thread.sleep(3000);
		
		
		List<JSONObject> linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId());
		
		int staticCounter=0;
		while(linkMongo.size()==0 && staticCounter<=400) {
			
			Thread.sleep(2000);
			System.out.println(
					"Going to sleep while waiting for link statics to bve created(sec): 10");

			linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId());
			staticCounter = staticCounter + 10;
			
		}
		
	
		GetAnalyticsMongoDTO analyticsActual = smsadaptor.getAnalyticsById(campaign.getCampaign().getId(), 200,
				uniqueIdentifier);
		
		if(analyticsActual.getLinkClickedCount()>1)
		{
			ReportHelper.logValidationFailure("Campaign status", "6",String.valueOf(analyticsActual.getLinkClickedCount()), "incorrect status ");
			Assert.assertTrue(false);
		}
		
	}
	@Test(priority = 12)
	public void verifylinkSchedulerforValidityExpired() throws InterruptedException, JsonProcessingException
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, null,templateURL,2,ValidityUnit.MINUTES);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		
		// update mongo date to past date
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), Instant.now().minus(2, ChronoUnit.HOURS));
		
		Thread.sleep(1000);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,shortCode,null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,shortCode,Instant.now().minus(4, ChronoUnit.MINUTES));
			
		
		
		List<JSONObject> linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId());
		if(linkMongo.size()!=0)
		{
			ReportHelper.logValidationFailure("Link trackling expired orders ", "0",String.valueOf(linkMongo.size()), "incorrect size ");
			Assert.assertTrue(false);
		}
		
		
		
		
		
	}
	@Test(priority = 13)
	public void verifyIncorrectURlinSubmitCampaign()
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, null, peId, headerId, null, date, null,"www.googl.bo.in.bo",2,ValidityUnit.DAYS);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 400, uniqueIdentifier);
		
		
	}
	
	@Test(priority = 14)
	public void verifyValidityinDays() throws InterruptedException, JsonProcessingException
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date, null,templateURL,3,ValidityUnit.DAYS);
		FetchCampaignDTO campaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),	orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		
		smsadaptor.approveCampaignbyId(campaign.getCampaign().getId(), 200, uniqueIdentifier);
		
		// update mongo date to past date
		smsMongo.updateStartDateinCampaign(campaign.getCampaign().getId(), Instant.now().minus(2, ChronoUnit.HOURS));
		
		
		
		Instant backstartDate=Instant.now().minus(2,ChronoUnit.DAYS);
		
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.IN_PROGRESS,shortCode,null);
		apiManger.pushStatusEventinKafka(campaign.getCampaign().getId(), CampaignExecutionStatus.COMPLETED,shortCode,backstartDate);
		
		Thread.sleep(7000);
		GetLinkTrackingMongoDTO linkMongo=	smsMongo.getLinkTrackingById(campaign.getCampaign().getId(),1);
		
		if(linkMongo.getLinkStatistics().isEmpty())
		{
			ReportHelper.logValidationFailure("Campaign status", "2",String.valueOf(linkMongo.getLinkStatistics().size()), "incorrect status ");
			Assert.assertTrue(false);
		}
		
		
		GetAnalyticsMongoDTO analyticsActual = smsadaptor.getAnalyticsById(campaign.getCampaign().getId(), 200,
				uniqueIdentifier);
		
		if(analyticsActual.getLinkClickedCount()<=0)
		{
			ReportHelper.logValidationFailure("Analytics Link Clicked", "12",String.valueOf(analyticsActual.getLinkClickedCount()), "incorrect Value in analytics ");
			Assert.assertTrue(false);
		}
	}
	
	
	
	
	

}
