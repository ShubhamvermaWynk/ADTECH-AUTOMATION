package com.airtel.RestAssured.Adtech.Tests.sms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
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
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.TemplateStatus;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.manager.SmsAdtechApiManager;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;

import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.teams.common.CommonApi;
import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;
import com.airtel.helper.report.ReportHelper;

import io.restassured.response.Response;

public class SmsDLTTest extends ReportHelper {

	CommonApi commonApi = new CommonApi();
	UserCompanyResponseVo userCompanyResponse;
	
	OrderGlobalVariables orderGlobalVariables;


	String bigImgFile;
	String uniqueIdentifier;
	Connection billingCon;

	Properties smsDataPropertyFile;
	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;
	SmsAdaptor smsadaptor;
	SmsAdMongo smsMongo;
	SmsAdtechApiManager apiManger;

	String peId, headerId, templateId, template;
	
	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	static Instant date;
	List<String> industries;
	String templatesWithSameHeader;
	String incorrectPanExtenionFile;

	String serverInitials;

	String dltJsonIgnore;
	OnboardUserAdaptor onboard;
	OnboardApiManager onboardApiManager;

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

		bigImgFile = envPropertyFile.getProperty("BIG_PAN_FILE_SIZE");
		incorrectPanExtenionFile = envPropertyFile.getProperty("PAN_FILE_INCORRECTEXT");
		smsadaptor = new SmsAdaptorImpl(envPropertyFile);
		smsMongo = new SmsAdMongoImpl(envPropertyFile);
		apiManger = new SmsAdtechApiManager(envPropertyFile, smsDataPropertyFile);

		peId = smsDataPropertyFile.getProperty("PEID");
		headerId = smsDataPropertyFile.getProperty("HEADER_ID");
		templateId = smsDataPropertyFile.getProperty("TEMPLATEID");
		template = smsDataPropertyFile.getProperty("TEMPLATE");
		dltJsonIgnore = smsDataPropertyFile.getProperty("DLT_JSON_IGNORE");

		date = Instant.now().plus(2, ChronoUnit.DAYS);

		lat = Double.valueOf(smsDataPropertyFile.getProperty("LAT"));
		longi = Double.valueOf(smsDataPropertyFile.getProperty("LONGI"));
		radius = Long.valueOf(smsDataPropertyFile.getProperty("RADIUS"));
		audienceReachCount = Long.valueOf(smsDataPropertyFile.getProperty("REACH_COUNT"));
		budget = new BigDecimal(smsDataPropertyFile.getProperty("BUDGET"));

		templatesWithSameHeader = smsDataPropertyFile.getProperty("TEMPLATE_ID_WITH_SAME_HEADER");
	}

	@BeforeMethod

	public void createUserCompany() {
		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		Response response = onboard.fetchUserByUuid(orderGlobalVariables.getOnboardingServerInitials(), orderGlobalVariables.getUserUUid(),
				"KYC,ORG", uniqueIdentifier);
		userCompanyResponse = commonApi.convertFromJson(response.asString(), UserCompanyResponseVo.class);

		FetchIndustryDTO industry = smsadaptor.fetchIndustry(uniqueIdentifier);
		industries = industry.getIndustries().stream().map(x -> x.getKey()).collect(Collectors.toList());

	}

	@Test(priority = 1)
	public void registerandvalidateDlt() {

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);

		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		PartnerSMSProfileDTO partnerProfile = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());
		PartnerSMSProfileDTO expectedProfile = apiManger.expectedDLTMongo(orderGlobalVariables,
				DLTProfileStatus.SUBMITTED, true, OrganisationType.GOVERNMENT,
				userCompanyResponse.getCompany().getTradeName(), userCompanyResponse.getCompany().getPanNumber(), false,
				partnerProfile.getContract().getDocumentKey(), null, partnerProfile.getCreatedAt(),
				partnerProfile.getUpdatedAt(), partnerProfile.getContract().getCreatedAt(),
				partnerProfile.getSubmissionDate());

		commonApi.compareObjectswithObjectMapper(expectedProfile, partnerProfile, dltJsonIgnore);

		FetchDLTInfoDTO actualData = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.SUBMITTED,
				true, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), false, null, false);
		commonApi.compareObjectswithObjectMapper(expectedData, actualData, dltJsonIgnore);
	}

	@Test(priority = 2)
	public void registerDLTwithPeid() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		PartnerSMSProfileDTO partnerProfile = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		PartnerSMSProfileDTO expectedProfile = apiManger.expectedDLTMongo(orderGlobalVariables,
				DLTProfileStatus.DLT_VERIFIED, true, OrganisationType.GOVERNMENT,
				userCompanyResponse.getCompany().getTradeName(), userCompanyResponse.getCompany().getPanNumber(), true,
				null, TemplateStatus.APPROVED, partnerProfile.getCreatedAt(), partnerProfile.getUpdatedAt(), null,
				partnerProfile.getSubmissionDate());

		commonApi.compareObjectswithObjectMapper(expectedProfile, partnerProfile, null);

		FetchDLTInfoDTO actualData = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				true, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), true, TemplateStatus.APPROVED, false);
		commonApi.compareObjectswithObjectMapper(expectedData, actualData, null);
	}



	@Test(priority = 3)
	public void registerDltwithDraftStatus() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);

		PartnerSMSProfileDTO partnerProfile = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());
		PartnerSMSProfileDTO expectedProfile = apiManger.expectedDLTMongo(orderGlobalVariables, DLTProfileStatus.DRAFT,
				false, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), false, partnerProfile.getContract().getDocumentKey(),
				null, partnerProfile.getCreatedAt(), partnerProfile.getUpdatedAt(),
				partnerProfile.getContract().getCreatedAt(), null);


		commonApi.compareObjectswithObjectMapper(expectedProfile, partnerProfile, dltJsonIgnore);

	}

	@Test(priority = 4)
	public void registerDltandCampaignwithDraftStatus() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		PartnerSMSProfileDTO mongoPartnerObj = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, false, 200, uniqueIdentifier);

		CamapignMongoDTO mongoCampaignobj = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		if (mongoCampaignobj.getStatus() != CampaignStatus.DRAFT
				&& mongoPartnerObj.getStatus() != DLTProfileStatus.DRAFT) {
			ReportHelper.logValidationFailure("Fetch Campaigb and DLT", "DRAFT",
					mongoCampaignobj.getStatus() + " " + mongoPartnerObj.getStatus(), "incorrect status ");
			Assert.assertTrue(false);
		}

	}

	@Test(priority = 5)
	public void validateCampaignandDLTWithSubmittedStatus() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, false, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		PartnerSMSProfileDTO mongoPartnerObj = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		System.out.println("responseCampaign" + responseCampaign);
		CamapignMongoDTO mongoCampaignobj = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		if (!(mongoCampaignobj.getStatus().equals(CampaignStatus.SUBMITTED))
				&& !(mongoPartnerObj.getStatus().equals(DLTProfileStatus.SUBMITTED))) {
			ReportHelper.logValidationFailure("Fetch Campaigb and DLT", "SUBMITTED",
					mongoCampaignobj.getStatus() + " " + mongoPartnerObj.getStatus(), "incorrect status ");
			Assert.assertTrue(false);
		}

	}

	@Test(priority = 6)
	public void approveandverifyCampignandDLTwithPeid() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		// Call admin api and approve campaign
		smsadaptor.approveCampaignbyId(responseCampaign.getCampaign().getId(), 200, uniqueIdentifier);

		CamapignMongoDTO mongoCampaignobj = smsMongo.getCampaignByCampaignId(responseCampaign.getCampaign().getId());
		PartnerSMSProfileDTO mongoPartnerObj = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		if (!(mongoCampaignobj.getStatus().equals(CampaignStatus.APPROVED))) {
			ReportHelper.logValidationFailure("Fetch Campaign and DLT", "APPROVED",
					String.valueOf(mongoCampaignobj.getStatus()), "incorrect status ");
			Assert.assertTrue(false);
		}
		if (!(mongoPartnerObj.getStatus().equals(DLTProfileStatus.DLT_VERIFIED))) {
			ReportHelper.logValidationFailure("Fetch Campaign and DLT", "DLT_VERIFIED",
					String.valueOf(mongoPartnerObj.getStatus()), "incorrect status ");
			Assert.assertTrue(false);

		}

		// Verify template status should be approved
		FetchDLTInfoDTO actualData = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				false, null, null, null, true, TemplateStatus.APPROVED, false);
		commonApi.compareObjectswithObjectMapper(expectedData, actualData, null);

	}

	@SuppressWarnings("unlikely-arg-type")
	@Test(priority = 7)
	public void approveandverifyCampignandDLTwithPan() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		// admin api approve and all peid details
		smsadaptor.approveDLT(responseCampaign.getCampaign().getId(), headerId, templateId, template, peId,
				orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		PartnerSMSProfileDTO mongoPartnerObj = smsMongo.getDLTByComapnyId(orderGlobalVariables.getCompanyUuid());

		if (!(mongoPartnerObj.getStatus().equals(DLTProfileStatus.DLT_VERIFIED))) {
			ReportHelper.logValidationFailure("Fetch Campaign and DLT", "DLT_VERIFIED",
					String.valueOf(mongoPartnerObj.getStatus()), "incorrect status ");
			Assert.assertTrue(false);
		}

		// Verify template status should be approved
		if (!(mongoPartnerObj.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().get(0).getStatus()
				.equals(TemplateStatus.APPROVED))) {

			ReportHelper.logValidationFailure("template Status", "APPROVED",
					String.valueOf(
							mongoPartnerObj.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().get(0).getStatus()),
					"incorrect status ");
			Assert.assertTrue(false);
		}

		FetchDLTInfoDTO actualData = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		FetchDLTInfoDTO expectedData = apiManger.expectedFetchDLT(orderGlobalVariables, DLTProfileStatus.DLT_VERIFIED,
				true, OrganisationType.GOVERNMENT, userCompanyResponse.getCompany().getTradeName(),
				userCompanyResponse.getCompany().getPanNumber(), false, TemplateStatus.APPROVED, true);
		commonApi.compareObjectswithObjectMapper(expectedData, actualData, dltJsonIgnore);

	}

	@Test(priority = 8)
	public void verifyPeidWithInvalidData() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				"146456456", headerId, template, "8783732", 400, uniqueIdentifier);

	}

	@Test(priority = 9)
	public void registerDltMultipleTimeswithSingleUser() {
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 200, uniqueIdentifier);
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 400, uniqueIdentifier);
		smsadaptor.registerDLTwithPeid(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), peId,
				headerId, template, templateId, 400, uniqueIdentifier);

	}

	@Test(priority = 10)
	public void headerIdwithMultipleTemplates() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
		smsMongo.updateEmailMobile(orderGlobalVariables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);

		SubmitCampaignDTO createObj = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, null, null, null, null, date, null, template);
		FetchCampaignDTO responseCampaign = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj, null, true, 200, uniqueIdentifier);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smsadaptor.approveDLT(responseCampaign.getCampaign().getId(), headerId, templateId, template, peId,
				orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		SubmitCampaignDTO createObj2 = apiManger.createCampaignobj(null, budget, industries, lat, longi, radius,
				audienceReachCount, templateId, peId, headerId, null, date, null, template);
		FetchCampaignDTO Campaign2 = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj2, null, true, 200, uniqueIdentifier);
		smsadaptor.approveDLT(Campaign2.getCampaign().getId(), headerId, templatesWithSameHeader.split(",")[0],
				template, peId, orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		FetchCampaignDTO Campaign3 = smsadaptor.submitCampaign(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), createObj2, null, true, 200, uniqueIdentifier);
		smsadaptor.approveDLT(Campaign3.getCampaign().getId(), headerId, templatesWithSameHeader.split(",")[1],
				template, peId, orderGlobalVariables.getCompanyUuid(), 200, uniqueIdentifier);

		FetchDLTInfoDTO actualData = smsadaptor.fetchDLT(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);
		if (actualData.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size() != 3) {
			ReportHelper.logValidationFailure("Template Size ", "3",
					String.valueOf(actualData.getDltInfo().getHeaderInfo().get(0).getTemplateMeta().size()),
					"incorrect status in fetch invoice");
			Assert.assertTrue(false);

		}
	}

	@Test(priority = 11)
	public void sendOtp() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, orderGlobalVariables.getPanPathName(), OrganisationType.GOVERNMENT, true, 200,
				uniqueIdentifier);
		smsadaptor.fetchContract(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), "LOA",
				uniqueIdentifier);
		smsadaptor.sendOTP(uniqueIdentifier, orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				"SMS");
		smsadaptor.sendOTP(uniqueIdentifier, orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				"EMAIL");
	}

	@Test(priority = 12)
	public void validateOtp() {
		smsadaptor.validateOTP(uniqueIdentifier, orderGlobalVariables.getCompanyUuid(), "SMS", 405);
		smsadaptor.validateOTP(uniqueIdentifier, orderGlobalVariables.getCompanyUuid(), "EMAIL", 405);
	}

	@Test(priority = 13)
	public void incorrectPanExtension() {
		smsadaptor.registerDLTwithPan(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),
				userCompanyResponse, incorrectPanExtenionFile, OrganisationType.GOVERNMENT, false, 400,
				uniqueIdentifier);
	}
}