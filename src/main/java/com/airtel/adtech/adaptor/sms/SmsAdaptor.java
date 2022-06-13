package com.airtel.adtech.adaptor.sms;

import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.FetchIndustryDTO;
import com.airtel.adtech.dto.response.sms.FetchTargetandBudgetDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderListDTO;
import com.airtel.common.dto.response.UserCompanyResponseVo;

import io.restassured.response.Response;

public interface SmsAdaptor {

	Response registerDLTwithPan(String companyUuid, String userId, UserCompanyResponseVo userCompany, String panPath,
			OrganisationType orgType, boolean acceptedFlag, int expectedStatusCode, String uniqueIdentifer);

	Response registerDLTwithPeid(String companyUuid, String userId, String peId, String headerId, String template,
			String templateId, int expectedStatusCode, String uniqueIdentifer);

	FetchCampaignDTO submitCampaign(String companyUuid, String userId, SubmitCampaignDTO campaignObj, String campaignId,
			boolean submitFlag, int expectedStatusCode, String uniqueIdentifer);

	FetchTargetandBudgetDTO submitBudgetandTarget(double lat, double longitude, long radius, String industry,
			int expectedStatusCode, String companyUUid, String userId, String uniqueIdentifer);

	FetchCampaignDTO fetchCampaign(String companyUuid, String userId, String campaignId, String uniqueIdentifer);

	FetchDLTInfoDTO fetchDLT(String companyUuid, String userId, String uniqueIdentifer);

	Response sendOTP(String uniqueIdentifer, String companyUuid, String userId, String type);

	Response validateOTP(String uniqueIdentifer, String companyUuid, String type, int expectedStatus);

	FetchIndustryDTO fetchIndustry(String uniqueIdentifer);

	Response fetchContract(String companyUuid, String userId, String type, String uniqueIdentifer);

	SmsOrderListDTO fetchOrderList(String companyUuid, String userId, String uniqueIdentifer);

	Response registerHeaders(String companyUuid, String userId, String peId, String headerId, String template,
			String templateId, int expectedStatusCode, String uniqueIdentifer);

	Response reviseDLTwithPan(String companyUuid, String userId, UserCompanyResponseVo userCompany, String panPath,
			OrganisationType orgType, boolean acceptedFlag, int expectedStatusCode, String uniqueIdentifer);

	// Admin Api's
	Response approveDLT(String campaignId, String headerId, String templateId, String template, String peId,
			String companyuuid, int expectedStatusCode, String uniqueIdentifer);

	Response approveCampaign(String campaignId, String templateId, String template, int expectedStatusCode,
			String uniqueIdentifer);

	Response approveCampaignbyId(String campaignId, int expectedStatusCode, String uniqueIdentifer);

	GetAnalyticsMongoDTO getAnalyticsById(String campaignId, int expectedStatusCode, String uniqueIdentifer);

	// Test SMS DTO
	Response testSMS(String userId, String companyId, String phoneNumber, String template, String templateId,
			String headerId, int expectedStatusCode, String uniqueIdentifer);
}
