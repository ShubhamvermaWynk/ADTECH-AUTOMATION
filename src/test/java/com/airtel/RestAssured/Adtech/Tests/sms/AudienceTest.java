package com.airtel.RestAssured.Adtech.Tests.sms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.airtel.adtech.adaptor.sms.AudeinceAdaptor;
import com.airtel.adtech.adaptor.sms.AudienceAdaptorImpl;
import com.airtel.adtech.adaptor.sms.SmsAdaptor;
import com.airtel.adtech.adaptor.sms.SmsAdaptorImpl;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserAdaptor;
import com.airtel.adtech.adaptor.userOnboard.OnboardUserIAdaptormpl;
import com.airtel.adtech.constants.enums.AudienceStatus;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.dto.response.sms.FetchTargetandBudgetDTO;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;
import com.airtel.helper.report.ReportHelper;

import com.airtel.teams.common.CommonApi;


import io.restassured.response.Response;



public class AudienceTest extends ReportHelper {
	CommonApi commonApi = new CommonApi();
	String uniqueIdentifier;
	Connection billingCon;
	SmsAdtechApiManager apiManger;
	SmsAdaptor smsadaptor;
	AudeinceAdaptor audienceadaptor;
	SmsAdMongo smsMongo;

	Properties smsDataPropertyFile;
	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;
	
	String peId,headerId,templateId,template;

	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	Instant date;
	List<String> industriesKeys;
	String filepath,audienceId;
	List<String> industriesValues;
	String serverInitials;
	String audienceIgnoreJson;
	
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
		orderDataPropertyFile = commonApi.getTestDataConfigPropertyObject("order");
		onboardingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("onboarding");
		paymentDataPropertyFile = commonApi.getTestDataConfigPropertyObject("payment");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);
		
		uniqueIdentifier = CommonApi.uniqueidentifier();
		
		smsadaptor= new SmsAdaptorImpl(envPropertyFile);
		audienceadaptor= new AudienceAdaptorImpl(envPropertyFile);
		smsMongo= new SmsAdMongoImpl(envPropertyFile);
		apiManger = new SmsAdtechApiManager(envPropertyFile, smsDataPropertyFile);
		
		peId= smsDataPropertyFile.getProperty("PEID");
	    headerId= smsDataPropertyFile.getProperty("HEADER_ID");
		templateId= smsDataPropertyFile.getProperty("TEMPLATEID");
		template= smsDataPropertyFile.getProperty("TEMPLATE");
		
		lat = Double.valueOf(smsDataPropertyFile.getProperty("LAT"));
		longi = Double.valueOf(smsDataPropertyFile.getProperty("LONGI"));
		radius = Long.valueOf(smsDataPropertyFile.getProperty("RADIUS"));
		audienceReachCount = Long.valueOf(smsDataPropertyFile.getProperty("REACH_COUNT"));
		budget = new BigDecimal(smsDataPropertyFile.getProperty("BUDGET"));

		date = Instant.now().plus(2, ChronoUnit.DAYS);
		onboard= new OnboardUserIAdaptormpl();
		
		
		audienceIgnoreJson=smsDataPropertyFile.getProperty("AUDIENCE_IGNORE_PATH");

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
				headerId, template, templateId, 200, uniqueIdentifier);

	}

	
	@Test
	public void createCamapignandVerifyAudienceinMongo()
	{
		
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		CamapignMongoDTO mongoObj= smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		
		AudienceMongoDTO actualObjMongo= smsMongo.getAudienceById(mongoObj.getAudience());
		AudienceMongoDTO expectedObjMongo= apiManger.expectedAudienceObj(mongoObj.getAudience(), AudienceStatus.DRAFT, industriesKeys, industriesValues, null);
		commonApi.compareObjects(expectedObjMongo, actualObjMongo, null);
		
		
		
	}

	@Test
	public void fetchAudience()
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		CamapignMongoDTO mongoObj= smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		
		AudienceMongoDTO actualObjResp= audienceadaptor.fetchAudienceById(mongoObj.getAudience(), uniqueIdentifier);
		AudienceMongoDTO expectedObjResp= apiManger.expectedAudienceObj(mongoObj.getAudience(), AudienceStatus.DRAFT, industriesKeys, industriesValues, null);
		commonApi.compareObjects(expectedObjResp, actualObjResp, audienceIgnoreJson);
		
		
	}
	
	@Test
	public void verifyApproveAudience() throws InterruptedException
	{

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);
		CamapignMongoDTO mongoObj= smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		
		
		//Appprove campaign
		smsadaptor.approveCampaignbyId(responseCampaign.getCampaign().getId(), 200, uniqueIdentifier);
		
		Thread.sleep(9000);
		AudienceMongoDTO actualObj= audienceadaptor.fetchAudienceById(mongoObj.getAudience(), uniqueIdentifier);
		AudienceMongoDTO expectedObj= apiManger.expectedAudienceObj(mongoObj.getAudience(), AudienceStatus.FILE_CREATION_COMPLETED, industriesKeys, industriesValues, "/app/online/audience-manager/"+mongoObj.getAudience()+".csv");
		commonApi.compareObjects(expectedObj, actualObj, audienceIgnoreJson);
		
		
		
	}
	
	@Test
	public void verifyAudienceonUpdatedOrder()
	{
		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industriesKeys, lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);
		smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), createObj, null, false, 200, uniqueIdentifier);
		
		
		
		SubmitCampaignDTO updatedObj = apiManger.createCampaignobj(null, budget, Arrays.asList("BANKING","FOOD_LOVERS"), lat, longi, radius,audienceReachCount, templateId, peId, headerId, null, date,null,template);

		FetchCampaignDTO updatedCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), updatedObj, null, true, 200, uniqueIdentifier);
		CamapignMongoDTO mongoObj= smsMongo.getCampaignByCampaignId(updatedCampaign.getCampaign().getId());
		
		AudienceMongoDTO actualObj= audienceadaptor.fetchAudienceById(mongoObj.getAudience(), uniqueIdentifier);
		AudienceMongoDTO expectedObj= apiManger.expectedAudienceObj(mongoObj.getAudience(), AudienceStatus.DRAFT, Arrays.asList("BANKING","FOOD_LOVERS"), Arrays.asList("Banking","Food Lovers"), null);
		commonApi.compareObjects(expectedObj, actualObj, audienceIgnoreJson);
		
		
		
	}
	
	@Test
	public void getAudienceCount()
	{
		FetchTargetandBudgetDTO budget= smsadaptor.submitBudgetandTarget(lat, longi, radius, industriesKeys.get(0), 200,orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		
		if(budget.getAudienceReachCount()==0)
		{
			ReportHelper.logValidationFailure("Expected Reach Count", "value of count", String.valueOf(budget.getAudienceReachCount()), "incorrect status ");
			Assert.assertTrue(false);
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}
