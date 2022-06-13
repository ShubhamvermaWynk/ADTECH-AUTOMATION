package com.airtel.adtech.manager;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.constants.enums.AdminOrderListKey;
import com.airtel.adtech.constants.enums.AdminOrderSortByKey;
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.SortDirection;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.request.sms.AdminOrderListDTO;
import com.airtel.adtech.dto.response.sms.AdminCampaignListDTO;
import com.airtel.adtech.dto.response.sms.AdminDLTListDTO;
import com.airtel.adtech.dto.response.sms.AdminOrderListResponseDTO;
import com.airtel.adtech.dto.response.sms.AdminPartnerDetailsDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.mongo.sms.AdminAdMongo;
import com.airtel.adtech.mongo.sms.AdminAdMongoImpl;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.teams.common.CommonApi;
import com.airtel.common.variables.OrderGlobalVariables;
import com.fasterxml.jackson.core.type.TypeReference;


public class AdminAdtechApiManager extends CommonApi implements AdtechApiConstants {

	AdminAdMongo adminMongo;
	SmsAdMongo smsMongo;

	public AdminAdtechApiManager(Properties envPropertyFile, Properties testDataProperties) {
		adminMongo = new AdminAdMongoImpl(envPropertyFile);
		smsMongo= new SmsAdMongoImpl(envPropertyFile);
		
	}

	public AdminOrderListDTO orderListRequestObj(AdminOrderListKey listType, String status, String serachValue,
			SortDirection direction, AdminOrderSortByKey sortBy) {
		AdminOrderListDTO orderList = new AdminOrderListDTO();

		AdminOrderListDTO.GroupBy groupObj = new AdminOrderListDTO.GroupBy();
		groupObj.setGroupId(listType);
		groupObj.setValue(status);

		if (serachValue != null) {
			AdminOrderListDTO.Filters filterObj = new AdminOrderListDTO.Filters();
			AdminOrderListDTO.Payload value = new AdminOrderListDTO.Payload();
			value.setValue(serachValue);
			filterObj.setPayload(value);
			orderList.setFilters(Arrays.asList(filterObj));
		}

		AdminOrderListDTO.SortBy sortObj = new AdminOrderListDTO.SortBy();
		sortObj.setDirection(direction);
		sortObj.setKey(sortBy);

		orderList.setGroupBy(groupObj);

		orderList.setSortBy(sortObj);
		return orderList;

	}

	public AdminOrderListResponseDTO getAdminList(AdminOrderListDTO adminListReq, int pageNo, int limit,boolean regex,String regexKey) {
		
		AdminOrderListResponseDTO adminList = new AdminOrderListResponseDTO();

		switch (adminListReq.getGroupBy().getGroupId()) {
		
		case DLT:
			adminList.setKey(adminListReq.getGroupBy().getGroupId());
			
			if(regex){getPartnerAdminListBySearch(adminListReq, adminList, pageNo, limit,regexKey);}
			
			else{getpartnerOrderList(adminListReq, adminList, pageNo, limit);}
			break;

		case CAMPAIGN:
			adminList.setKey(adminListReq.getGroupBy().getGroupId());
			
			if(regex) {getCampaignAdminListBySearch(adminListReq, adminList, pageNo, limit,regexKey);}
			
			else{getCampaignOrderList(adminListReq, adminList, pageNo, limit);}
			break;
		}

		return adminList;
	}

	public void getpartnerOrderList(AdminOrderListDTO adminListReq, AdminOrderListResponseDTO adminList, int pageNo,
			int limit) {

		List<PartnerSMSProfileDTO> dltList = adminMongo.getSortedAdminDLTList(adminListReq.getGroupBy().getValue(),
				adminListReq.getSortBy().getKey(), String.valueOf(adminListReq.getSortBy().getDirection()), pageNo,
				limit);

		List<AdminDLTListDTO> dltAdminDataList = convertToAdminDLTList(dltList);

		String jsonData = convertToJsonwithObjectMapper(dltAdminDataList);

		List<Map<String, Object>> orderMap = convertFromJson(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});

		adminList.setData(orderMap);
		List<JSONObject> list = adminMongo.getAdminDLTListwithStatus(adminListReq.getGroupBy().getValue());
		adminList.setTotalRecordsCount(list.size());

	}

	public void getCampaignOrderList(AdminOrderListDTO adminListReq, AdminOrderListResponseDTO adminList, int pageNo,
			int limit) {

		List<CamapignMongoDTO> campaignList = adminMongo.getSortedAdminCampaignList(
				adminListReq.getGroupBy().getValue(), adminListReq.getSortBy().getKey(),
				String.valueOf(adminListReq.getSortBy().getDirection()), pageNo, limit);
		
		

		List<AdminCampaignListDTO> CampaignAdminDataList = convertToAdminCampaignList(campaignList);

		String jsonData = convertToJsonwithObjectMapper(CampaignAdminDataList);

		List<Map<String, Object>> orderMap = convertFromJson(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});

		adminList.setData(orderMap);

		List<JSONObject> list = adminMongo.getAdminCampaignListwithStatus(adminListReq.getGroupBy().getValue());
		adminList.setTotalRecordsCount(list.size());

	}

	public List<AdminDLTListDTO> convertToAdminDLTList(List<PartnerSMSProfileDTO> partnerList) {

		List<AdminDLTListDTO> dltList = new ArrayList<>();

		for (PartnerSMSProfileDTO partner : partnerList) {

			AdminDLTListDTO dlt = new AdminDLTListDTO();
			dlt.setCompanyName(partner.getCompanyDetails().getCompanyName());
			dlt.setCompanyUuid(partner.getCompanyUuid());
			dlt.setContactName(partner.getProfile().getContactName());
			dlt.setEmail(partner.getProfile().getEmail());

			dlt.setStatus(partner.getStatus());

			dlt.setUpdatedAt(partner.getUpdatedAt());
			dlt.setCreatedAt(partner.getCreatedAt());

			if(partner.getDltInfo()!=null)
			{dlt.setPeId(partner.getDltInfo().getPeId());}

			
			dlt.setSubmissionDate(partner.getSubmissionDate());
			dltList.add(dlt);
		}

		return dltList;
	}

	public List<AdminCampaignListDTO> convertToAdminCampaignList(List<CamapignMongoDTO> campaignList) {
		List<AdminCampaignListDTO> list = new ArrayList<>();

		
		
		for (CamapignMongoDTO campaignData : campaignList) {
			AdminCampaignListDTO adminCampaign = new AdminCampaignListDTO();
			adminCampaign.setCampaignId(campaignData.getCampaignId());
			adminCampaign.setCampaignName(campaignData.getTitle());
			
			adminCampaign.setCreatedAt(campaignData.getCreatedAt());
			adminCampaign.setExecutionDate(campaignData.getCampaignDuration().getStartDate());
			

			
		//	adminCampaign.setSlotTime((campaignData.getCampaignDuration().getSlotBooking().getStartSlotTime()-campaignData.getCampaignDuration().getSlotBooking().getStartSlotTime()));
			adminCampaign.setUpdatedAt(campaignData.getUpdatedAt());
			
			PartnerSMSProfileDTO partner= smsMongo.getDLTByComapnyId(campaignData.getCompanyUuid());
			if(partner.getCompanyDetails()!=null) {
			adminCampaign.setCompanyName(partner.getCompanyDetails().getCompanyName());}
			
			
			list.add(adminCampaign);
		}

		return list;

	}

	public void getCampaignAdminListBySearch(AdminOrderListDTO adminListReq, AdminOrderListResponseDTO adminList,int pageNo, int limit,String regexKey) {
		
	
		List<CamapignMongoDTO> campaignList = adminMongo.getAdminCampaignListWithRegexSortStatusPage(adminListReq.getFilters().get(0).getPayload().getValue(),adminListReq.getGroupBy().getValue(), adminListReq.getSortBy().getKey(),String.valueOf(adminListReq.getSortBy().getDirection()), pageNo, limit);
		
		List<AdminCampaignListDTO> CampaignAdminDataList = convertToAdminCampaignList(campaignList);

		String jsonData = convertToJsonwithObjectMapper(CampaignAdminDataList);

		List<Map<String, Object>> orderMap = convertFromJson(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});

		adminList.setData(orderMap);
		
		List<JSONObject> jsonList= adminMongo.getAdminCampaignListwithStatusandRegex(regexKey,adminListReq.getFilters().get(0).getPayload().getValue(),adminListReq.getGroupBy().getValue());
		adminList.setTotalRecordsCount(jsonList.size());

	}


	
	public void getPartnerAdminListBySearch(AdminOrderListDTO adminListReq, AdminOrderListResponseDTO adminList, int pageNo,int limit,String regexKey) {
	
		List<PartnerSMSProfileDTO> dltList = adminMongo.getAdminDLTListWithRegexSortStatusPage(adminListReq.getFilters().get(0).getPayload().getValue(), adminListReq.getSortBy().getKey(),String.valueOf(adminListReq.getSortBy().getDirection()), pageNo, limit);
				
		List<AdminDLTListDTO> dltAdminDataList = convertToAdminDLTList(dltList);

		String jsonData = convertToJsonwithObjectMapper(dltAdminDataList);

		List<Map<String, Object>> orderMap = convertFromJson(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});

	
			
		adminList.setData(orderMap.size()!=0 ?orderMap :null);
		
		List<JSONObject> jsonList= adminMongo.getAdminDLTListwithStatusandRegex(regexKey,adminListReq.getFilters().get(0).getPayload().getValue(),adminListReq.getGroupBy().getValue());
		adminList.setTotalRecordsCount(jsonList.size());

	}
	
	public AdminPartnerDetailsDTO getAdminDltDetails(OrderGlobalVariables orderGlobalVariables,FetchDLTInfoDTO expectedData,Instant submissionDate,boolean campaign )
	{
		AdminPartnerDetailsDTO expectedDltDetails= new AdminPartnerDetailsDTO();
		expectedDltDetails.setDltInfo(expectedData.getDltInfo());
		expectedDltDetails.setLoa(expectedData.getLoa());
		expectedDltDetails.setRegistrationDetails(expectedData.getRegistrationDetails());
		expectedDltDetails.setStatus(expectedData.getStatus());
		expectedDltDetails.setUserUuid(orderGlobalVariables.getUserUUid());
		expectedDltDetails.setCompanyUuid(orderGlobalVariables.getCompanyUuid());
		expectedDltDetails.setCampaignSubmitted(campaign);
		expectedDltDetails.setSubmissionDate(submissionDate);
		

		
		
		
		return expectedDltDetails;
	}
	
	

}
