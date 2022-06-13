package com.airtel.adtech.mongo.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.airtel.adtech.constants.enums.AdminOrderListKey;
import com.airtel.adtech.constants.enums.AdminOrderSortByKey;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.response.sms.AdminDLTListDTO;
import com.airtel.adtech.dto.response.sms.AdminOrderListResponseDTO;
import com.airtel.teams.common.CommonApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.BasicDBObject;

public class AdminAdMongoImpl extends CommonApi implements AdminAdMongo {

	String[] mongoMultiserverInitials;
	int mongoport;
	static String dltSubmittedStatus = "SUBMITTED";
	String adtechdatabaseInitials, dltCollection, campaignCollection, audienceCollection, analyticsCollection,
			linktrackingCollection;

	public AdminAdMongoImpl(Properties envPropertyFile) {
		mongoMultiserverInitials = envPropertyFile.getProperty("MONGO_CREDENTAILS_CLUSTER").split(",");
		mongoport = Integer.valueOf(envPropertyFile.getProperty("PORT"));

		adtechdatabaseInitials = envPropertyFile.getProperty("ADTECH_SMS_DATABASE_NAME");
		dltCollection = envPropertyFile.getProperty("DLT_COLLECTION_NAME");
		campaignCollection = envPropertyFile.getProperty("CAMPAIGN_COLLECTION_NAME");
		audienceCollection = envPropertyFile.getProperty("AUDIENCE_COLLECTION_NAME");
		analyticsCollection = envPropertyFile.getProperty("ANALYTICS_COLLECTION_NAME");
		linktrackingCollection = envPropertyFile.getProperty("LINK_COLLECTION_NAME");

	}

	@Override
	public List<PartnerSMSProfileDTO> getSortedAdminDLTList(String status, AdminOrderSortByKey sortKey,
			String sortValue, int pageNo, int limit) {

		Map<String, Object> mongoQuery = new HashMap<>();
		mongoQuery.put("status", status);

		List<JSONObject> jsonObjectList = getSorteddatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, dltCollection, String.valueOf(sortKey), sortValue == "ASC" ? 1 : -1,
				pageNo, limit);

		List<PartnerSMSProfileDTO> list = new ArrayList<>();

		TypeReference<PartnerSMSProfileDTO> listref = new TypeReference<PartnerSMSProfileDTO>() {
		};
		for (JSONObject json : jsonObjectList) {
			PartnerSMSProfileDTO dltObj = convertFromJson(json.toString(), listref);
			list.add(dltObj);

		}
		return list;

	}

	@Override
	public List<JSONObject> getAdminDLTListwithStatus(String status) {

		Map<String, Object> mongoQuery = new HashMap<>();
		mongoQuery.put("status", status);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapperObject(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, dltCollection);

		return jsonObjectList;
	}

	@Override
	public List<CamapignMongoDTO> getSortedAdminCampaignList(String status, AdminOrderSortByKey sortKey,
			String sortValue, int pageNo, int limit) {
		Map<String, Object> mongoQuery = new HashMap<>();
		mongoQuery.put("status", status);
		mongoQuery.put("partnerSmsProfile.peId", new BasicDBObject("$exists", true));

		List<JSONObject> jsonObjectList = getSorteddatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, campaignCollection, String.valueOf(sortKey),
				sortValue == "ASC" ? 1 : -1, pageNo, limit);

		List<CamapignMongoDTO> list = new ArrayList<>();

		TypeReference<CamapignMongoDTO> listref = new TypeReference<CamapignMongoDTO>() {
		};
		for (JSONObject json : jsonObjectList) {
			CamapignMongoDTO dltObj = convertFromJson(json.toString(), listref);
			list.add(dltObj);

		}
		return list;
	}

	@Override
	public List<JSONObject> getAdminCampaignListwithStatus(String status) {
		Map<String, Object> mongoQuery = new HashMap<>();
		mongoQuery.put("status", status);
		mongoQuery.put("partnerSmsProfile.peId", new BasicDBObject("$exists", true));

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapperObject(mongoMultiserverInitials, mongoport,
				mongoQuery, adtechdatabaseInitials, campaignCollection);

		return jsonObjectList;
	}

	public List<CamapignMongoDTO> getAdminCampaignListWithRegexSortStatusPage(String searchValue, String campaignStatus,
			AdminOrderSortByKey sortKey, String sortValue, int pageNo, int limit) {

		Map<String, String> regexObj = new HashMap<>();
		regexObj.put("title", searchValue);
		regexObj.put("templateSmsContent", searchValue);
	
		


		List<JSONObject> jsonObjectList = getdatafrommongowithRegexandExistsFilterObjectMapper(mongoMultiserverInitials, mongoport,
				regexObj, adtechdatabaseInitials, campaignCollection, String.valueOf(sortKey),
				sortValue == "ASC" ? 1 : -1, pageNo, limit, dltSubmittedStatus ,"partnerSmsProfile.peId");

		List<CamapignMongoDTO> list = new ArrayList<>();

		TypeReference<CamapignMongoDTO> listref = new TypeReference<CamapignMongoDTO>() {
		};
		for (JSONObject json : jsonObjectList) {
			CamapignMongoDTO dltObj = convertFromJson(json.toString(), listref);
			list.add(dltObj);

		}
		
		return list;
	}

	public List<PartnerSMSProfileDTO> getAdminDLTListWithRegexSortStatusPage(String searchValue, AdminOrderSortByKey sortKey,
			String sortValue, int pageNo, int limit) {

		Map<String, String> mongoQuery = new HashMap<>();

		mongoQuery.put("profile.contactName", searchValue);
		mongoQuery.put("profile.email", searchValue);
		mongoQuery.put("companyDetails.companyName", searchValue);

		List<JSONObject> jsonObjectList = getdatafrommongowithRegexObjectMapper(mongoMultiserverInitials, mongoport,mongoQuery, adtechdatabaseInitials, dltCollection, String.valueOf(sortKey), sortValue == "ASC" ? 1 : -1,	pageNo, limit, dltSubmittedStatus);

		List<PartnerSMSProfileDTO> list = new ArrayList<>();

		TypeReference<PartnerSMSProfileDTO> listref = new TypeReference<PartnerSMSProfileDTO>() {
		};
		for (JSONObject json : jsonObjectList) {
			PartnerSMSProfileDTO dltObj = convertFromJson(json.toString(), listref);
			list.add(dltObj);

		}
		return list;

	}

	@Override
	public List<JSONObject> getAdminCampaignListwithStatusandRegex(String key,String searchValue, String campaignStatus) {

		Map<String, String> regexObj = new HashMap<>();

		regexObj.put(key, searchValue);

		List<JSONObject> jsonObjectList = getdatafrommongowithRegexObjectMapper(mongoMultiserverInitials, mongoport,
				regexObj, adtechdatabaseInitials, campaignCollection, campaignStatus,"partnerSmsProfile.peId");
		return jsonObjectList;
	}

	
	@Override
	public List<JSONObject> getAdminDLTListwithStatusandRegex(String key,String searchValue, String campaignStatus) {

		Map<String, String> regexObj = new HashMap<>();

		regexObj.put(key, searchValue);

		List<JSONObject> jsonObjectList = getdatafrommongowithRegexObjectMapper(mongoMultiserverInitials, mongoport,
				regexObj, adtechdatabaseInitials, dltCollection, campaignStatus,null);
		return jsonObjectList;
	}
}