package com.airtel.adtech.mongo.sms;

import java.time.Instant;
import java.util.List;

import org.json.JSONObject;

import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.mongo.GetLinkTrackingMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.mongo.TestSMSTrackCountDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderDTO;

public interface SmsAdMongo {

	CamapignMongoDTO getCampaignByCampaignId(String campaignId);

	List<CamapignMongoDTO> getCamapignByComapnyId(String companyId);

	void updateCampaignStatus(String campaignId, CampaignStatus status);

	PartnerSMSProfileDTO getDLTByComapnyId(String companyId);

	AudienceMongoDTO getAudienceById(String audienceId);

	void updateTwoFAbyCompanyId(String companyId, boolean flag);

	void updateEmailMobile(String companyId, boolean flag);

	List<SmsOrderDTO> getOrderList(String companyId);

	void updateFilePathinAudienceByid(String audienceId, String filepath);

	void updateStartDateinCampaign(String campaignId, Instant localDateTime);

	GetAnalyticsMongoDTO getAnalyticsById(String campaignId);

	GetLinkTrackingMongoDTO getLinkTrackingById(String campaignId, int size);

	List<JSONObject> getLinkTrackingById(String campaignId);

	List<JSONObject> getAnalyticsByIdJson(String campaignId);

//	Test_SMS_Track_Count MongoDB Collection DTO and update TrackCount method implementation

	TestSMSTrackCountDTO getTrackCountByUserId(String userId);

	void updateTrackCountByUserId(String userId, int count);

	void updateExpiryDate(String userId, Instant expiryDate);
}