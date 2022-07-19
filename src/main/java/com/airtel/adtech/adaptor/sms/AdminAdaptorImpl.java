package com.airtel.adtech.adaptor.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.testng.Assert;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.request.sms.AdminDLTApprovalDTO;
import com.airtel.adtech.dto.request.sms.AdminOrderListDTO;
import com.airtel.adtech.dto.response.sms.AdminPartnerDetailsDTO;
import com.airtel.adtech.dto.response.sms.AdminTagPeidDTO;
import com.airtel.teams.common.CommonApi;

import io.restassured.response.Response;

public class AdminAdaptorImpl  extends CommonApi implements AdminAdaptor,AdtechApiConstants{

	
	
	String serverInitials;
	String uniqueIdentifer;
	
	
	
	public AdminAdaptorImpl(Properties envProperty) {
		serverInitials = envProperty.getProperty("ADTECH_SMS");
		uniqueIdentifer = uniqueidentifier();
		
	}
	
	

	@Override
	public AdminPartnerDetailsDTO fetchDltAdmin(String companyUuid,int expectedStatusCode) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("companyUuid", companyUuid);
		
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams, FETCH_DLT_ADMIN, false,
				false, null, null, uniqueIdentifer);
		if(response.getStatusCode()!=expectedStatusCode)
		{
			Assert.assertTrue(false, "Incorrect Status code");
		}
		
		AdminPartnerDetailsDTO partner= convertFromJsonwithObjectMapperwithclass(response.asString(), AdminPartnerDetailsDTO.class);

		return partner;
	}

	@Override
	public AdminTagPeidDTO tagPeid(String peid, String companyUuid, int expectedStatusCode,boolean isCampaign) {
		
		Map<String, String> obj= new HashMap<>();
		obj.put("peId", peid);
		obj.put("companyUuid", companyUuid);
		
		String body= convertToJson(obj);
	
		Response response = getPostResponseWithStatusCode(body, serverInitials, TAG_PEID ,true, expectedStatusCode, null, null, uniqueIdentifer);
		
		PartnerSMSProfileDTO partner= convertFromJsonwithObjectMapperwithclass(response.asString(), PartnerSMSProfileDTO.class);
		
		AdminTagPeidDTO adminDlt= new AdminTagPeidDTO();
		adminDlt.setPartnerSMSProfileDto(partner);
		adminDlt.setCampaignCreated(isCampaign);
		
		
		
		return adminDlt;
	}

	@Override
	public Response notifyUser(String companyUuid) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("companyUuid", companyUuid);
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,NOTIFY_USER, false, false, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response reviseDlt(String companyUuid, String reason, int expectedStatusCode) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("companyUuid", companyUuid);
		
		Map<String, String> obj= new HashMap<>();
		obj.put("reviseReason", reason);
		String body= convertToJson(obj);
		
		Response response = getPostResponseWithStatusCodeandParams(body, serverInitials,
				REVISE_DLT , false, expectedStatusCode, null, null, queryParams, uniqueIdentifer);

		return response;
	}

	@Override
	public Response reviseCampaign(String campaignId,int expectedStatusCode) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("campaignId", campaignId);
		
		Map<String, String> obj= new HashMap<>();
		obj.put("reviseReason", "REVISE_CAMPAIGN");
		String body= convertToJson(obj);
		
		
		Response response = getPostResponseWithStatusCodeandParams(body, serverInitials,
				REVISE_CAMPAIGN , false, expectedStatusCode, null, null, queryParams, uniqueIdentifer);
		return response;
	}

	@Override
	public Response approveCampaign(String campaignId, String peId, String headerId, String templateId, String template,int expectedStatusCode) {
		
		AdminDLTApprovalDTO dltObj = new AdminDLTApprovalDTO();

		dltObj.setCampaignId(campaignId);
		dltObj.setPeId(peId);
		dltObj.setHeaderId(headerId);
		dltObj.setTemplate(template);
		dltObj.setTemplateId(templateId);
		

		String body = convertToJson(dltObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, APPROVE_CAMPAIGN_ADMIN, true, expectedStatusCode,
				null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response getCampaignDetails(String campaignId) {
		
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				FETCH_CAMPAIGN_ADMIN + "?"+ campaignId , false, false, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response fetchAnalyticsAdmin(String campaignId) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("campaignId", campaignId);
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				FETCH_ANALYTICS_ADMIN , false, false, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response downloadFile(String filePath, DocumentType documentType,int expectedStatusCode) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("file", filePath);
		queryParams.put("documentType", documentType.name());
		
		String body= convertToJson(queryParams);
		
		Response response = getPostResponseWithStatusCodeandParams(body, serverInitials,
				DOWNLOAD_FILE , false, expectedStatusCode, null, null, null, uniqueIdentifer);
		return response;
	}



	@Override
	public Response fetchorderList(AdminOrderListDTO adminObj,Integer pageNo, Integer offset, int expectedStatusCode) {
		
	
		Map<String, String> queryParams = new HashMap<>();
		
		queryParams.put("pageNo", String.valueOf(pageNo));
		queryParams.put("offset", String.valueOf(offset));
		
		
		
		String body= convertToJson(adminObj);
		
		Response response = getPostResponseWithStatusCodeandParams(body, serverInitials,
				FETCH_ORDERLIST_ADMIN , false, expectedStatusCode, null, null, queryParams, uniqueIdentifer);
		return response;
	}




}
