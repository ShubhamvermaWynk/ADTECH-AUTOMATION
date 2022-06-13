package com.airtel.adtech.mongo.sms;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.testng.Assert;

import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.GetAnalyticsMongoDTO;
import com.airtel.adtech.dto.mongo.GetLinkTrackingMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.mongo.TestSMSTrackCountDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderDTO;
import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;

public class SmsAdMongoImpl extends CommonApi implements SmsAdMongo {

	CommonApi commonApi = new CommonApi();
	String[] mongoMultiserverInitials;
	int mongoport;
	String adtechdatabaseInitials, dltCollection, campaignCollection, audienceCollection, analyticsCollection,
			linktrackingCollection, testSmsTrackCount;

	public SmsAdMongoImpl(Properties envPropertyFile) {
		mongoMultiserverInitials = envPropertyFile.getProperty("MONGO_CREDENTAILS_CLUSTER").split(",");
		mongoport = Integer.valueOf(envPropertyFile.getProperty("PORT"));

		adtechdatabaseInitials = envPropertyFile.getProperty("ADTECH_SMS_DATABASE_NAME");
		dltCollection = envPropertyFile.getProperty("DLT_COLLECTION_NAME");
		campaignCollection = envPropertyFile.getProperty("CAMPAIGN_COLLECTION_NAME");
		audienceCollection = envPropertyFile.getProperty("AUDIENCE_COLLECTION_NAME");

		analyticsCollection = envPropertyFile.getProperty("ANALYTICS_COLLECTION_NAME");
		linktrackingCollection = envPropertyFile.getProperty("LINK_COLLECTION_NAME");
		testSmsTrackCount = envPropertyFile.getProperty("SMS_COUNT_COLLECTION");

	}

	@Override
	public CamapignMongoDTO getCampaignByCampaignId(String campaignId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("campaignId", campaignId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, campaignCollection);

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("invoice Data size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		CamapignMongoDTO campaignData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				CamapignMongoDTO.class);

		return campaignData;

	}

	@Override
	public List<CamapignMongoDTO> getCamapignByComapnyId(String companyId) {

		List<CamapignMongoDTO> list = new ArrayList<>();

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("companyUuid", companyId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, campaignCollection);

		if (jsonObjectList.size() < 1) {
			ReportHelper.logValidationFailure("invoice Data size", "greater than 1",
					String.valueOf(jsonObjectList.size()), "collection value should be greater than 1");
			Assert.assertTrue(false);
		}
		CamapignMongoDTO campaignData = new CamapignMongoDTO();

		for (JSONObject json : jsonObjectList) {
			campaignData = convertFromJsonwithObjectMapperwithclass(json.toString(), CamapignMongoDTO.class);
			list.add(campaignData);
		}

		return list;
	}

	@Override
	public PartnerSMSProfileDTO getDLTByComapnyId(String companyId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("companyUuid", companyId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, dltCollection);

		System.out.println("JsonList" + jsonObjectList.toString());

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("invoice Data size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		PartnerSMSProfileDTO profileData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				PartnerSMSProfileDTO.class);

		return profileData;
	}

	@Override
	public AudienceMongoDTO getAudienceById(String audienceId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("_id", audienceId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, audienceCollection);

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("invoice Data size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		AudienceMongoDTO audienceData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				AudienceMongoDTO.class);

		return audienceData;
	}

	@Override
	public void updateTwoFAbyCompanyId(String companyId, boolean flag) {

		Map<String, String> object = new HashMap<>();
		object.put("profile.twoFaVerified", "true");

		String updatedMongoObj = convertToJsonwithObjectMapper(object);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, dltCollection, "companyUuid",
				companyId, updatedMongoObj);
	}

	@Override
	public List<SmsOrderDTO> getOrderList(String companyId) {

		List<SmsOrderDTO> orderList = new ArrayList<>();

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("companyUuid", companyId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, campaignCollection);

		for (JSONObject json : jsonObjectList) {
			SmsOrderDTO campaignData = convertFromJsonwithObjectMapperwithclass(json.toString(), SmsOrderDTO.class);

			orderList.add(campaignData);
		}

		return orderList;
	}

	@Override
	public void updateFilePathinAudienceByid(String audienceId, String filepath) {

		Map<String, String> object = new HashMap<>();
		object.put("filePath", filepath);

		String updatedMongoObj = convertToJsonwithObjectMapper(object);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, audienceCollection, "_id",
				audienceId, updatedMongoObj);

	}

	@Override
	public void updateStartDateinCampaign(String campaignId, Instant date) {

		Map<String, Instant> object = new HashMap<>();
		object.put("campaignDuration.startDate", date);

		// String updatedMongoObj = convertToJsonwithObjectMapper(object);

		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, campaignCollection, "campaignId",
				campaignId, object);
	}

	@Override
	public void updateEmailMobile(String companyId, boolean flag) {

		Map<String, String> object = new HashMap<>();
		object.put("profile.mobileVerified", "true");
		object.put("profile.emailVerified", "true");

		String updatedMongoObj = convertToJsonwithObjectMapper(object);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, dltCollection, "companyUuid",
				companyId, updatedMongoObj);

	}

	@Override
	public GetAnalyticsMongoDTO getAnalyticsById(String campaignId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("campaignId", campaignId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, analyticsCollection);

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("Analytics size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		GetAnalyticsMongoDTO audienceData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				GetAnalyticsMongoDTO.class);

		return audienceData;
	}

	public List<JSONObject> getAnalyticsByIdJson(String campaignId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("campaignId", campaignId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, analyticsCollection);

		return jsonObjectList;
	}

	@Override
	public GetLinkTrackingMongoDTO getLinkTrackingById(String campaignId, int size) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("campaignId", campaignId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, linktrackingCollection);

		if (jsonObjectList.size() != size) {
			ReportHelper.logValidationFailure("Campaign Statistics size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		GetLinkTrackingMongoDTO audienceData = convertFromJsonwithObjectMapperwithclass(
				jsonObjectList.get(0).toString(), GetLinkTrackingMongoDTO.class);

		return audienceData;
	}

	@Override
	public List<JSONObject> getLinkTrackingById(String campaignId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("campaignId", campaignId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, linktrackingCollection);

		return jsonObjectList;
	}

//	Test SMS Track Count Collection Implementation
	@Override
	public TestSMSTrackCountDTO getTrackCountByUserId(String userId) {
		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("userId", userId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, testSmsTrackCount);

		System.out.println("JsonList" + jsonObjectList.toString());

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("invoice Data size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		TestSMSTrackCountDTO trackCount = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				TestSMSTrackCountDTO.class);

		return trackCount;
	}

	@Override
	public void updateTrackCountByUserId(String userId, int count) {
		Map<String, Integer> object = new HashMap<>();
		object.put("remainingSms", count);

		String updatedMongoObj = convertToJsonwithObjectMapper(object);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, testSmsTrackCount, "userId",
				userId, updatedMongoObj);
	}

	@Override
	public void updateExpiryDate(String userId, Instant expiryDate) {
		Map<String, Instant> map = new HashMap<>();
		map.put("expireAt", expiryDate);

		String updateMongoObj = convertToJsonwithObjectMapper(map);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, testSmsTrackCount, "userId",
				userId, updateMongoObj);

	}

	@Override
	public void updateCampaignStatus(String campaignId, CampaignStatus status) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", status);

		String updateMongoObj = convertToJsonwithObjectMapper(map);
		updateMongoData(mongoMultiserverInitials, mongoport, adtechdatabaseInitials, campaignCollection, "campaignId",
				campaignId, updateMongoObj);

	}
}
