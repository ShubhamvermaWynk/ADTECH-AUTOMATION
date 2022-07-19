package com.airtel.adtech.adaptor.sms;

import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.dto.request.sms.AdminOrderListDTO;
import com.airtel.adtech.dto.response.sms.AdminPartnerDetailsDTO;
import com.airtel.adtech.dto.response.sms.AdminTagPeidDTO;

import io.restassured.response.Response;

public interface AdminAdaptor {
	
	Response fetchorderList(AdminOrderListDTO adminObj,Integer pageNo, Integer offset,int expectedStatusCode);
	AdminPartnerDetailsDTO fetchDltAdmin(String companyUuid, int expectedStatusCode);
	AdminTagPeidDTO tagPeid(String peid,String companyUuid,int expectedStatusCode,boolean isCampaign );
	Response notifyUser(String companyUuid);
	Response reviseDlt(String companyUuid,String reason,int expectedStatusCode);
	Response reviseCampaign(String campaignId,int expectedStatusCode);
	Response approveCampaign(String campaignId,String peId,String headerId,String templateId,String template,int expectedStatusCode);
	Response getCampaignDetails(String campaignId);
	Response fetchAnalyticsAdmin(String campaignId);
	Response downloadFile(String filePath, DocumentType documentType ,int expectedStatusCode);
	
}

