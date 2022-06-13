package com.airtel.adtech.mongo.sms;



import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.airtel.adtech.constants.enums.AdminOrderSortByKey;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;


public interface AdminAdMongo {
	
	List<PartnerSMSProfileDTO> getSortedAdminDLTList(String status, AdminOrderSortByKey sortKey, String sortValue, int pageNo,int limit);
	List<CamapignMongoDTO> getSortedAdminCampaignList(String status, AdminOrderSortByKey sortKey, String sortValue,int pageNo,int limit);
	
	List<JSONObject> getAdminDLTListwithStatus(String status);
	List<JSONObject> getAdminCampaignListwithStatus(String status);
	
	List<CamapignMongoDTO> getAdminCampaignListWithRegexSortStatusPage(String searchValue,String campaignStatus, AdminOrderSortByKey sortKey, String sortValue,int pageNo,int limit);
	List<PartnerSMSProfileDTO> getAdminDLTListWithRegexSortStatusPage(String searchValue, AdminOrderSortByKey sortKey,String sortValue, int pageNo, int limit);
	
	List<JSONObject> getAdminCampaignListwithStatusandRegex(String key,String searchValue,String campaignStatus);
	List<JSONObject> getAdminDLTListwithStatusandRegex(String key,String searchValue, String campaignStatus);
	
	
	

}
