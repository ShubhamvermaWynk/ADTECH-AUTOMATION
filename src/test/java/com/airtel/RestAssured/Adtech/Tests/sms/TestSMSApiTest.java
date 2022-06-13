package com.airtel.RestAssured.Adtech.Tests.sms;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

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
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.dto.mongo.TestSMSTrackCountDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.common.variables.OrderGlobalVariables;
import com.airtel.helper.report.ReportHelper;

import com.airtel.teams.common.CommonApi;

import io.restassured.response.Response;

import com.airtel.common.variables.InitOrderVariables;

public class TestSMSApiTest extends ReportHelper {
	CommonApi commonApi = new CommonApi();

	Properties smsDataPropertyFile, envPropertyFile, onboardingDataPropertyFile;
	SmsAdaptor smsadaptor;
	OnboardUserAdaptor onboard;
	SmsAdMongo smsMongo;
	SmsAdtechApiManager apiManger;
	TestSMSTrackCountDTO testSms;
	Response response;
	String scope, serverInitials, uniqueIdentifier, templateIds, headerIds, templateURL;
	String peId, headerId, template, templateId;
	int remainingSms;
	Instant expiryDate;
	OnboardApiManager onboardApiManager;   
	OrderGlobalVariables orderGlobalVariables;
	UserCompanyResponseVo userCompanyResponse;

	@Parameters({ "environment" })
	@BeforeClass
	public void setEnvironment(@Optional("qa_1") String environment) {

		envPropertyFile = commonApi.getConfigPropertyObject(environment);
		onboard = new OnboardUserIAdaptormpl();
		onboardApiManager = new OnboardApiManager(envPropertyFile);

		smsDataPropertyFile = commonApi.getTestDataConfigPropertyObject("adtechSms");
		serverInitials = envPropertyFile.getProperty("USERS_URL");
		onboardingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("onboarding");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);

		onboard = new OnboardUserIAdaptormpl();

		uniqueIdentifier = CommonApi.uniqueidentifier();

		smsadaptor = new SmsAdaptorImpl(envPropertyFile);
		smsMongo = new SmsAdMongoImpl(envPropertyFile);
		apiManger = new SmsAdtechApiManager(envPropertyFile, smsDataPropertyFile);

		peId = smsDataPropertyFile.getProperty("PEID");
		headerId = smsDataPropertyFile.getProperty("HEADER_ID");
		templateId = smsDataPropertyFile.getProperty("TEMPLATEID");
		template = smsDataPropertyFile.getProperty("TEMPLATE");
		scope = "KYC,ORG";
		remainingSms = 14;
		templateIds = smsDataPropertyFile.getProperty("TEMPLATE_IDS");
		headerIds = smsDataPropertyFile.getProperty("HEADER_IDS");
		templateURL = smsDataPropertyFile.getProperty("TEMPLATE_WITH_URL");
	}

	@BeforeMethod
	public void createUserCompany() {

		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		Response response = onboard.fetchUserByUuid(serverInitials, orderGlobalVariables.getUserUUid(), "KYC,ORG",
				uniqueIdentifier);
		userCompanyResponse = commonApi.convertFromJson(response.asString(), UserCompanyResponseVo.class);

	}

	@Test(priority = 1)
	public void testSMSSend() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 200, uniqueIdentifier);
	}

	@Test(priority = 2)
	public void testSMSWithDraftStatus() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);

		FetchDLTInfoDTO dltStatus = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);

		System.out.println("DLT Status:\t" + dltStatus.getStatus());
//	DLT_STATUS : DRAFT
		if (dltStatus.getStatus() == DLTProfileStatus.DRAFT) {
			response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
					orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 400, uniqueIdentifier);
			Assert.assertEquals(response.jsonPath().get("displayMessage"),
					"Please confirm the DLT details or try in sometime");
		}
	}

	@Test(priority = 3)
	public void testSMSWithSubmittedStatus() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);

		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
//	Set Phone and Email fields as Verified
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		FetchDLTInfoDTO dltStatus = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);

		System.out.println("DLT Status:\t" + dltStatus.getStatus());
//	DLT_STATUS : SUBMITTED
		if (dltStatus.getStatus() == DLTProfileStatus.SUBMITTED) {
			response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
					orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 400, uniqueIdentifier);
			Assert.assertEquals(response.jsonPath().get("displayMessage"),
					"Please confirm the DLT details or try in sometime");
		}
	}

	@Test(priority = 4)
	public void testSMSwithMultipleTemplates() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		smsadaptor.registerHeaders(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerIds.split(",")[0], templateURL, templateIds.split(",")[0], 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), templateURL, templateIds.split(",")[0], headerIds.split(",")[0],
				200, uniqueIdentifier);
	}

	@Test(priority = 5)
	public void testSMSTrackCount() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 200, uniqueIdentifier);

		testSms = smsMongo.getTrackCountByUserId(orderGlobalVariables.getUserUUid());
		System.out.println("Remaining SMS Count:\t" + testSms.getRemainingSms());

		if (testSms.getRemainingSms() == remainingSms) {
			smsMongo.updateTrackCountByUserId(orderGlobalVariables.getUserUUid(), 0);
			response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
					orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 400, uniqueIdentifier);
			Assert.assertEquals(response.jsonPath().get("displayMessage"), "You have reached the daily limit (15)");
		} else {
			Assert.assertEquals(testSms.getRemainingSms(), remainingSms, "Incorrect SMS Count!");
			Assert.assertTrue(false);
		}
	}

	@Test(priority = 6)
	public void testSMSExpiryDate() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 200, uniqueIdentifier);

		testSms = smsMongo.getTrackCountByUserId(orderGlobalVariables.getUserUUid());

		smsMongo.updateExpiryDate(orderGlobalVariables.getUserUUid(), Instant.now().minus(1, ChronoUnit.DAYS));

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateId, headerId, 200, uniqueIdentifier);

		String[] expireDate = testSms.getExpireAt().toString().split("T");
		String finalExpiryDate = expireDate[0];
		System.out.println(finalExpiryDate);

		String[] currentDate = Instant.now().toString().split("T");
		String finalCurrentDate = currentDate[0];
		System.out.println(finalCurrentDate);

		Assert.assertEquals(finalExpiryDate, finalCurrentDate);
	}

	@Test(priority = 7)
	public void testSMSIncorrectTemplateId() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateIds.split(",")[0], headerId, 400,
				uniqueIdentifier);

		Assert.assertEquals(response.jsonPath().get("displayMessage"), "Template not approved yet !");
	}

	@Test(priority = 8)
	public void testSMSIncorrectHeaderId() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), template, templateId, headerIds.split(",")[0], 400,
				uniqueIdentifier);

		Assert.assertEquals(response.jsonPath().get("displayMessage"), "Incorrect Dlt details");
	}

	@Test(priority = 9)
	public void testSMSIncorrectTemplateContent() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), templateURL, templateId, headerId, 400, uniqueIdentifier);

		Assert.assertEquals(response.jsonPath().get("displayMessage"), "Template not approved yet !");
	}

	@Test(priority = 10)
	public void testSMSBlankBody() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getMobileNumber(), "", template, headerId, 400, uniqueIdentifier);
		Assert.assertEquals(response.jsonPath().get("displayMessage"), "must not be blank");
	}

	@Test(priority = 11)
	public void testSMSInvalidPhoneNumber() {
		response = smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), peId, headerId, template, templateId, 200, uniqueIdentifier);

		response = smsadaptor.testSMS(orderGlobalVariables.getUserUUid(), orderGlobalVariables.getCompanyUuid(),
				"95600268", template, templateId, headerId, 400, uniqueIdentifier);
		Assert.assertEquals(response.jsonPath().get("displayMessage"), "Invalid Phone Number");
	}
}