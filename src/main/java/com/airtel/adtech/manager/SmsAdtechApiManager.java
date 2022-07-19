package com.airtel.adtech.manager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


import org.json.JSONObject;

import com.airtel.adtech.adaptor.sms.SmsAdaptor;
import com.airtel.adtech.adaptor.sms.SmsAdaptorImpl;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.constants.enums.AudienceStatus;
import com.airtel.adtech.constants.enums.CampaignExecutionStatus;
import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.constants.enums.OrganisationType;
import com.airtel.adtech.constants.enums.TemplateStatus;
import com.airtel.adtech.constants.enums.ValidityUnit;
import com.airtel.adtech.dto.mongo.AudienceMongoDTO;
import com.airtel.adtech.dto.mongo.CamapignMongoDTO;
import com.airtel.adtech.dto.mongo.CompanyMongoDTO;
import com.airtel.adtech.dto.mongo.CompanyMongoDTO.Document;
import com.airtel.adtech.dto.mongo.ContractMongoDTO;
import com.airtel.adtech.dto.mongo.DLTMetaInfoMongoDTO;
import com.airtel.adtech.dto.mongo.DLTMetaInfoMongoDTO.SMSHeaderMongoDto;
import com.airtel.adtech.dto.mongo.DLTMetaInfoMongoDTO.SMSTemplateMongoDto;
import com.airtel.adtech.dto.mongo.GetLinkTrackingMongoDTO;
import com.airtel.adtech.dto.mongo.GetLinkTrackingMongoDTO.SmsCampaignLinkStatistics;
import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;
import com.airtel.adtech.dto.mongo.ProfileMongoInfoDTO;
import com.airtel.adtech.dto.request.sms.SmsExecutionKafKaEventDTO;
import com.airtel.adtech.dto.request.sms.SubmitAudiencetargeting;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO;
import com.airtel.adtech.dto.request.sms.SubmitCampaignDTO.DltDTO;
import com.airtel.adtech.dto.request.sms.TestSMSDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO.CampaignFetchDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO.DltFetchDTO;
import com.airtel.adtech.dto.response.sms.FetchCampaignDTO.TargetingFetchDTO;
import com.airtel.adtech.dto.response.sms.FetchDLTInfoDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderDTO;
import com.airtel.adtech.dto.response.sms.SmsOrderListDTO;
import com.airtel.adtech.entity.CampaignDTO;
import com.airtel.adtech.entity.CampaignDTO.Template;
import com.airtel.adtech.entity.CampaignDuration;
import com.airtel.adtech.entity.CampaignMetaData;
import com.airtel.adtech.entity.CampaignMetaData.SmsLinkDetails;
import com.airtel.adtech.entity.DLTContractInfoDTO;
import com.airtel.adtech.entity.DLTInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO.SMSHeaderDto;
import com.airtel.adtech.entity.DLTMetaInfoDTO.SMSTemplateDto;
import com.airtel.adtech.entity.DocumentEntity;
import com.airtel.adtech.entity.GeoLocation;
import com.airtel.adtech.entity.Industry;
import com.airtel.adtech.entity.PartnerSmsProfile;
import com.airtel.adtech.entity.TargetingDTO;
import com.airtel.adtech.entity.Validity;
import com.airtel.adtech.mongo.sms.SmsAdMongo;
import com.airtel.adtech.mongo.sms.SmsAdMongoImpl;
import com.airtel.common.dto.response.UserCompanyResponseVo;
import com.airtel.teams.common.CommonApi;
import com.airtel.common.variables.OrderGlobalVariables;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SmsAdtechApiManager extends CommonApi implements AdtechApiConstants {
	String peId, headerId, templateId, template;
	String title, location;
	BigDecimal budget;
	double lat, longi;
	long radius, audienceReachCount;
	String kafkaIp, kafkaTopic;
	int port;
	String shortCode;
	int linkclick;
	CampaignDTO campaign = new CampaignDTO();
	String templateIdsExpected, headerIdsExpected, templateURL;

	SmsAdaptor smsadaptor;
	SmsAdMongo smsMongo ;


	public SmsAdtechApiManager(Properties envProperty, Properties testDataProperties) {
		peId = testDataProperties.getProperty("PEID");
		headerId = testDataProperties.getProperty("HEADER_ID");
		templateId = testDataProperties.getProperty("TEMPLATEID");
		template = testDataProperties.getProperty("TEMPLATE");
		title = testDataProperties.getProperty("CAMPAIGN_NAME");
		location = testDataProperties.getProperty("LOCATION");

		lat = Double.valueOf(testDataProperties.getProperty("LAT"));
		longi = Double.valueOf(testDataProperties.getProperty("LONGI"));
		radius = Long.valueOf(testDataProperties.getProperty("RADIUS"));
		audienceReachCount = Long.valueOf(testDataProperties.getProperty("REACH_COUNT"));
		budget = new BigDecimal(testDataProperties.getProperty("BUDGET"));

		kafkaIp = envProperty.getProperty("KAFKA_IP");
		kafkaTopic = envProperty.getProperty("KAFKA_ADTECH_SMS_EVENT_TOPIC");
		port = Integer.parseInt(envProperty.getProperty("KAFKA_PORT"));

		shortCode = testDataProperties.getProperty("SHORT_CODE");
		linkclick = Integer.parseInt(testDataProperties.getProperty("LINK_CLICKED"));

		templateIdsExpected = testDataProperties.getProperty("TEMPLATE_IDS_DATA");
		headerIdsExpected = testDataProperties.getProperty("HEADER_IDS_DATA");
		templateURL = testDataProperties.getProperty("TEMPLATE_WITH_URL");

		smsadaptor = new SmsAdaptorImpl(envProperty);
		smsMongo = new SmsAdMongoImpl(envProperty);



	}

	public CamapignMongoDTO expectedCampaignMongo(String audienceId, String campaignId, String companyUuid,
			String userUuid, CampaignStatus status, BigDecimal budget, Instant campaigndate, Instant createdAt,
			Instant updatedAt, int validityValue, ValidityUnit unit, String template, Instant urlcreationDate) {
		CamapignMongoDTO campaignObj = new CamapignMongoDTO();

		PartnerSmsProfile partnerObj = new PartnerSmsProfile();
		CampaignDuration dateobj = new CampaignDuration();

		CampaignMetaData campaignMetadata = new CampaignMetaData();
		CampaignMetaData.SmsLinkDetails smsLinkDetails = new SmsLinkDetails();

		Validity validObj = new Validity();
		validObj.setUnit(unit);
		validObj.setValue(validityValue);

		smsLinkDetails.setValidity(validObj);

		if (urlcreationDate != null) {
			smsLinkDetails.setShortCode(Arrays.asList(shortCode));
			smsLinkDetails.setUrlCreationDate(urlcreationDate);

			campaignMetadata.setSmsLinkDetails(smsLinkDetails);

			campaignObj.setCampaignMetadata(campaignMetadata);
		}

		partnerObj.setHeaderId(headerId);
		partnerObj.setPeId(peId);
		partnerObj.setTemplate(template);
		partnerObj.setTemplateId(templateId);

		dateobj.setStartDate(campaigndate);

		campaignObj.setAudience(audienceId);
		campaignObj.setCampaignId(campaignId);
		campaignObj.setCompanyUuid(companyUuid);
		campaignObj.setEstimatedBudget(budget);
		campaignObj.setStatus(status);

		campaignObj.setTemplateSmsContent(template);
		campaignObj.setTitle(title);
		campaignObj.setType("SMS");
		campaignObj.setUserUuid(userUuid);
		campaignObj.setPartnerSmsProfile(partnerObj);
		campaignObj.setCampaignDuration(dateobj);

		campaignObj.setLastUpdatedBy(Arrays.asList(userUuid));
		campaignObj.setUpdatedAt(updatedAt);
		campaignObj.setCreatedAt(createdAt);

		return campaignObj;

	}

	public SubmitCampaignDTO createCampaignobj(String id, BigDecimal budget, List<String> indList, double lat,
			double longi, long radius, Long audienceReachCount, String templateId, String peId, String headerId,
			CampaignStatus status, Instant date, String timeslot, String templateContent) {
		SubmitCampaignDTO campaignObj = new SubmitCampaignDTO();

		CampaignDuration dateObj = new CampaignDuration();
		TargetingDTO targetObj = new TargetingDTO();

		GeoLocation geo = new GeoLocation();
		CampaignDTO.Template temp = new Template();

		geo.setLatitude(lat);
		geo.setLongitude(longi);
		geo.setCoverage(radius);
		geo.setLocationAddress(location);

		targetObj.setAudienceReachCount(audienceReachCount);
		targetObj.setGeo(geo);
		targetObj.setIndustry(indList);

		temp.setContent(templateContent);

		temp.setId(templateId != null ? templateId : null);

		campaign.setBudget(budget);

		dateObj.setStartDate(date);
		dateObj.setSlotTime(timeslot != null ? timeslot : null);

		campaign.setCampaignDuration(dateObj);

		if (id != null) {
			campaign.setId(id);
		}

		campaign.setTargeting(targetObj);
		campaign.setTemplateDetails(temp);
		campaign.setTitle(title);
		if (status != null) {
			campaign.setStatus(status);
		}

		DltDTO dlt = new DltDTO();

		if (headerId != null) {
			dlt.setHeaderId(headerId);
		}
		if (peId != null) {
			dlt.setPeId(peId);
		}
		if (headerId != null && peId != null) {
			campaignObj.setDlt(dlt);
		}

		campaignObj.setCampaign(campaign);

		return campaignObj;

	}

	public SubmitCampaignDTO createCampaignobj(String id, BigDecimal budget, List<String> indList, double lat,
			double longi, long radius, Long audienceReachCount, String templateId, String peId, String headerId,
			CampaignStatus status, Instant date, String timeslot, String templateContent, int validityValue,
			ValidityUnit unit) {

		SubmitCampaignDTO obj = createCampaignobj(id, budget, indList, lat, longi, radius, audienceReachCount,
				templateId, peId, headerId, status, date, timeslot, templateContent);

		CampaignMetaData campaignMetadata = new CampaignMetaData();
		CampaignMetaData.SmsLinkDetails smsLinkDetails = new SmsLinkDetails();

		Validity validObj = new Validity();
		validObj.setUnit(unit);
		validObj.setValue(validityValue);

		smsLinkDetails.setValidity(validObj);

		campaignMetadata.setSmsLinkDetails(smsLinkDetails);

		campaign.setCampaignMetadata(campaignMetadata);
		obj.setCampaign(campaign);

		return obj;
	}

	public FetchCampaignDTO expectedCampaignResponse(String id, BigDecimal budget, List<String> indListKey,
			List<String> indListValues, double lat, double longi, long radius, Long audienceReachCount,
			String templateId, String peId, String headerId, CampaignStatus status, Instant date, String timeSlot,
			int validityValue, ValidityUnit unit, String template) {

		FetchCampaignDTO campaignObj = new FetchCampaignDTO();

		FetchCampaignDTO.CampaignFetchDTO campaign = new CampaignFetchDTO();
		FetchCampaignDTO.DltFetchDTO dltObj = new DltFetchDTO();
		CampaignDuration dateObj = new CampaignDuration();
		FetchCampaignDTO.TargetingFetchDTO targetObj = new TargetingFetchDTO();

		List<Industry> indListCreated = new ArrayList<>();

		GeoLocation geo = new GeoLocation();
		CampaignDTO.Template temp = new Template();

		for (int i = 0; i < indListKey.size(); i++) {
			Industry indObj = new Industry();
			indObj.setDisplayName(indListValues.get(i));
			indObj.setKey(indListKey.get(i));
			indListCreated.add(indObj);

		}

		geo.setLatitude(lat);
		geo.setLongitude(longi);
		geo.setCoverage(radius);
		geo.setLocationAddress(location);

		targetObj.setAudienceReachCount(audienceReachCount);
		targetObj.setGeo(geo);
		targetObj.setIndustry(indListCreated);

		temp.setContent(template);

		temp.setId(templateId != null ? templateId : null);

		campaign.setBudget(budget);

		dateObj.setStartDate(date);
		dateObj.setSlotTime(timeSlot != null ? timeSlot : null);
		campaign.setCampaignDuration(dateObj);

		if (id != null) {
			campaign.setId(id);
		}

		campaign.setTargeting(targetObj);
		campaign.setTemplateDetails(temp);
		campaign.setTitle(title);

		if (status != null) {
			campaign.setStatus(status);
		}

		if (headerId != null) {
			dltObj.setHeaderId(headerId);
		}
		if (peId != null) {
			dltObj.setPeId(peId);
		}
		if (headerId != null && peId != null) {
			campaignObj.setDlt(dltObj);
		}

		CampaignMetaData campaignMetadata = new CampaignMetaData();

		CampaignMetaData.SmsLinkDetails smsLinkObj = new SmsLinkDetails();

		Validity validObj = new Validity();
		if (unit != null) {
			validObj.setUnit(unit);
		}
		if (validityValue != 0) {
			validObj.setValue(validityValue);
		}

		smsLinkObj.setValidity(validObj);
		campaignMetadata.setSmsLinkDetails(smsLinkObj);

		if (unit != null && validityValue != 0) {
			campaign.setCampaignMetadata(campaignMetadata);
		}
		campaignObj.setCampaign(campaign);

		return campaignObj;

	}

	public FetchDLTInfoDTO expectedFetchDLT(OrderGlobalVariables order, DLTProfileStatus status, boolean contractFlag,
			OrganisationType type, String companyName, String pan, boolean isPeid, TemplateStatus tempstatus,
			boolean approve) {

		FetchDLTInfoDTO obj = new FetchDLTInfoDTO();
		List<DocumentType> docTypesList = Arrays.asList(DocumentType.values());
		if (isPeid) {

			DLTMetaInfoDTO dltInfo = new DLTMetaInfoDTO();
			DLTMetaInfoDTO.SMSHeaderDto smsObj = new SMSHeaderDto();
			DLTMetaInfoDTO.SMSTemplateDto smsTempObj = new SMSTemplateDto();

			smsTempObj.setContent(template);
			smsTempObj.setId(templateId);

			// smsTempObj.setTemplateType("PROMOTIONAL");
			smsTempObj.setStatus(tempstatus);

			smsObj.setHeaderId(headerId);
			smsObj.setStatus("VERIFIED");
			smsObj.setTemplateMeta(Arrays.asList(smsTempObj));

			dltInfo.setPeId(peId);
			dltInfo.setHeaderInfo(Arrays.asList(smsObj));

			obj.setDltInfo(dltInfo);

		} else {

			DLTInfoDTO profileObj = new DLTInfoDTO();
			DLTContractInfoDTO loa = new DLTContractInfoDTO();

			profileObj.setCompanyName(companyName);
			profileObj.setCompanyType(type);
			profileObj.setEmailId(order.getEmailIdAd());
			profileObj.setGstn(order.getGstinAd());
			profileObj.setMobileNumber(order.getMobileNumberAd());
			profileObj.setName(order.getFirstName() + order.getLastName());
			profileObj.setPan(pan);

			for (DocumentType doctype : docTypesList) {
				DocumentEntity document = setdocumentObjResponse(doctype);
				profileObj.setAnyDocument(document, doctype);
			}

			loa.setAccepted(contractFlag);

			obj.setLoa(loa);
			obj.setRegistrationDetails(profileObj);
		}

		if (approve) {
			DLTMetaInfoDTO dltInfo = new DLTMetaInfoDTO();
			DLTMetaInfoDTO.SMSHeaderDto smsObj = new SMSHeaderDto();
			DLTMetaInfoDTO.SMSTemplateDto smsTempObj = new SMSTemplateDto();

			smsTempObj.setContent(template);
			smsTempObj.setId(templateId);

			// smsTempObj.setTemplateType("PROMOTIONAL");
			smsTempObj.setStatus(tempstatus);

			smsObj.setHeaderId(headerId);
			smsObj.setStatus("VERIFIED");
			smsObj.setTemplateMeta(Arrays.asList(smsTempObj));

			dltInfo.setPeId(peId);
			dltInfo.setHeaderInfo(Arrays.asList(smsObj));

			obj.setDltInfo(dltInfo);

		}

		obj.setStatus(status);

		return obj;

	}

	public DocumentEntity setdocumentObjResponse(DocumentType docType) {

		DocumentEntity document = new DocumentEntity();
		// document.setDocumentKey(ADDENDUM_CALLBACK_URL_PATH);
		document.setDocumentType(docType);
		document.setName("pan.jpg");
		document.setSize(20863);
		return document;

	}

	public PartnerSMSProfileDTO expectedDLTMongo(OrderGlobalVariables order, DLTProfileStatus status, boolean loaflag,
			OrganisationType type, String companyName, String pan, boolean isPeid, String docKey,
			TemplateStatus tempstatus, Instant createdAt, Instant updatedAt, Instant contractDate,Instant submissionDate) {


		PartnerSMSProfileDTO dlt = new PartnerSMSProfileDTO();
		List<DocumentType> docTypesList = Arrays.asList(DocumentType.values());

		if (isPeid) {
			DLTMetaInfoMongoDTO dltInfo = new DLTMetaInfoMongoDTO();
			DLTMetaInfoMongoDTO.SMSHeaderMongoDto smsObj = new SMSHeaderMongoDto();

			DLTMetaInfoMongoDTO.SMSTemplateMongoDto smsTempObj = new SMSTemplateMongoDto();

			smsTempObj.setContent(template);
			smsTempObj.set_id(templateId);

			// smsTempObj.setTemplateType("PROMOTIONAL");
			if (tempstatus != null) {
				smsTempObj.setStatus(tempstatus);
			}

			smsObj.setHeaderId(headerId);
			smsObj.setStatus("VERIFIED");
			smsObj.setTemplateMeta(Arrays.asList(smsTempObj));
			
			dltInfo.setPeId(peId);
			dltInfo.setHeaderInfo(Arrays.asList(smsObj));

			dlt.setAlreadyRegistered(false);
			dlt.setDltInfo(dltInfo);
			if(submissionDate!=null) {
			dlt.setSubmissionDate(submissionDate);}

		} else {
			ProfileMongoInfoDTO profile = new ProfileMongoInfoDTO();
			profile.setMobileNumber(order.getMobileNumberAd());
			profile.setEmail(order.getEmailIdAd());
			profile.setContactName(order.getFirstName() + order.getLastName());
			profile.setTwoFaVerified(loaflag);
			profile.setMobileVerified(loaflag);
			profile.setEmailVerified(loaflag);

			CompanyMongoDTO companyObj = new CompanyMongoDTO();
			companyObj.setCategory(type);
			companyObj.setCompanyName(companyName);
//			companyObj.setPan(pan);
//			companyObj.setGstn(order.getGlobalGstin());
			companyObj.setGSTN(order.getGlobalGstin());
			companyObj.setPAN(pan);

			for (DocumentType doctype : docTypesList) {
				Document document = setdocumentObj(doctype);
				companyObj.setAnyDocument(document, doctype);
			}

			ContractMongoDTO contractObj = new ContractMongoDTO();
			contractObj.setType(DocumentType.LOA);
			if (docKey != null) {
				contractObj.setDocumentKey(docKey);
			}

			if (contractDate != null) {
				contractObj.setCreatedAt(contractDate);
				contractObj.setUpdatedAt(contractDate);
			}
			contractObj.setAccepted(loaflag);

			dlt.setCompanyDetails(companyObj);
			dlt.setContract(contractObj);
			dlt.setProfile(profile);
			dlt.setAlreadyRegistered(false);
			if(submissionDate!=null) {
			dlt.setSubmissionDate(submissionDate);}
		}

		dlt.setCompanyUuid(order.getCompanyUuid());
		dlt.setUserUuid(order.getUserUUid());
		dlt.setStatus(status);
		dlt.setCreatedAt(createdAt);
		dlt.setUpdatedAt(updatedAt);
		
		return dlt;

	}

	public Document setdocumentObj(DocumentType docType) {

		Document document = new Document();
		document.setType(docType);
		// document.setPath(key);
		document.setOriginalName("pan.jpg");
		document.setSize(20863);
		return document;

	}
	
	public DocumentEntity setdocumentOb(DocumentType docType) {

		DocumentEntity document = new DocumentEntity();
		document.setDocumentType(docType);
		// document.setPath(key);
		document.setName("pan.jpg");
		document.setSize(20863);
		return document;

	}

	public AudienceMongoDTO expectedAudienceObj(String id, AudienceStatus status, List<String> indusKeys,
			List<String> indListValues, String filePath) {
		AudienceMongoDTO obj = new AudienceMongoDTO();
		SubmitAudiencetargeting targetAudience = new SubmitAudiencetargeting();

		GeoLocation geoLocation = new GeoLocation();

		List<Industry> indListCreated = new ArrayList<>();

		for (int i = 0; i < indusKeys.size(); i++) {
			Industry indObj = new Industry();
			indObj.setDisplayName(indListValues.get(i));
			indObj.setKey(indusKeys.get(i));
			indListCreated.add(indObj);

		}

		geoLocation.setLatitude(lat);
		geoLocation.setLongitude(longi);
		geoLocation.setCoverage(radius);
		geoLocation.setLocationAddress(location);

		targetAudience.setGeoLocation(geoLocation);
		targetAudience.setIndustry(indListCreated);

		obj.setAudienceTargetting(targetAudience);
		obj.setEstimatedReach(audienceReachCount);
		obj.setFilePath(filePath);
		obj.set_id(id);
		obj.setStatus(status);

		return obj;

	}

	public SmsOrderListDTO expectedOrderList(List<CamapignMongoDTO> mongoOrders) {
		SmsOrderListDTO list = new SmsOrderListDTO();

		List<SmsOrderDTO> pendingWithUserList = new ArrayList<SmsOrderDTO>();
		List<SmsOrderDTO> pendingWithAirtelList = new ArrayList<SmsOrderDTO>();
		List<SmsOrderDTO> confirmed = new ArrayList<SmsOrderDTO>();

		for (CamapignMongoDTO order : mongoOrders) {

			if (order.getStatus().equals(CampaignStatus.DRAFT)) {
				pendingWithUserList.add(setorderList(order));

			} else if (order.getStatus().equals(CampaignStatus.SUBMITTED)) {
				pendingWithAirtelList.add(setorderList(order));

			} else if (order.getStatus().equals(CampaignStatus.APPROVED)) {
				confirmed.add(setorderList(order));

			}

		}

		pendingWithUserList = pendingWithUserList.stream()
				.sorted((t1, t2) -> t2.getCampaignId().compareTo(t1.getCampaignId())).collect(Collectors.toList());
		pendingWithAirtelList = pendingWithAirtelList.stream()
				.sorted((t1, t2) -> t2.getCampaignId().compareTo(t1.getCampaignId())).collect(Collectors.toList());
		confirmed = confirmed.stream().sorted((t1, t2) -> t2.getCampaignId().compareTo(t1.getCampaignId()))
				.collect(Collectors.toList());

		list.setCONFIRMED(confirmed);
		list.setPENDING_WITH_AIRTEL(pendingWithAirtelList);
		list.setPENDING_WITH_USER(pendingWithUserList);

		return list;

	}

	public SmsOrderDTO setorderList(CamapignMongoDTO order) {
		SmsOrderDTO obj = new SmsOrderDTO();

		obj.setCampaignId(order.getCampaignId());
		obj.setCreatedAt(order.getCreatedAt());
		obj.setStartDate(order.getCampaignDuration().getStartDate());
		obj.setStatus(order.getStatus());
		obj.setTitle(order.getTitle());
		obj.setUpdatedAt(order.getUpdatedAt());

		return obj;

	}

	public void pushStatusEventinKafka(String campaignId, CampaignExecutionStatus status, String shortCode,
			Instant startDate) throws JsonProcessingException {
		SmsExecutionKafKaEventDTO sms = new SmsExecutionKafKaEventDTO();
		sms.setCampaignId(campaignId);
		sms.setStatus(status);
		sms.setEndTime(Instant.now());

		sms.setStartTime(startDate != null ? startDate : Instant.now());

		if (shortCode != null) {
			sms.setShortcodes(Arrays.asList(shortCode));
		}

//		JSONObject obj= new JSONObject();
//		obj.put("campaignId", campaignId);
//		obj.put("status", status);
//		obj.put("shortcodes",Arrays.asList(shortCode));
//		obj.put("startTime", Instant.now().toString());
//		obj.put("endTime", Instant.now().toString());

		CommonApi.produceKafkaEvent(kafkaIp, port, kafkaTopic, "campaignExecutorStatus", sms);
		System.out.println("Message sent successfully");

	}

	public GetLinkTrackingMongoDTO getexpectedLinkMongo(String campaignId, int value, ValidityUnit units,
			Instant instant) {
		GetLinkTrackingMongoDTO linkObj = new GetLinkTrackingMongoDTO();
		linkObj.setCampaignId(campaignId);
		linkObj.setShortCode(shortCode);
		linkObj.setUrlCreationDate(instant);

		GetLinkTrackingMongoDTO.SmsCampaignLinkStatistics statObj = new SmsCampaignLinkStatistics();
		statObj.setLinkClicked(linkclick);
		statObj.setStatus("SUCCESS");

		Validity validObj = new Validity();
		validObj.setUnit(units);
		validObj.setValue(value);

		linkObj.setValidity(validObj);
		linkObj.setLinkStatistics(Arrays.asList(statObj));
		return linkObj;

	}

	public FetchDLTInfoDTO expectedFetchDLTWithMultipleHeaders(OrderGlobalVariables order, DLTProfileStatus status,
			boolean contractFlag, OrganisationType type, String companyName, String pan, boolean isPeid,
			TemplateStatus tempstatus, boolean approve, String headerStatus) {

		FetchDLTInfoDTO obj = new FetchDLTInfoDTO();
		List<SMSHeaderDto> headerObjList = new ArrayList<>();
		DLTMetaInfoDTO dltInfo = new DLTMetaInfoDTO();
		List<DocumentType> docTypesList = Arrays.asList(DocumentType.values());

		if (isPeid) {
			for (int i = 0; i < 4; i++) {

				DLTMetaInfoDTO.SMSHeaderDto headerObj = new SMSHeaderDto();

				headerObj.setHeaderId(headerIdsExpected.split(",")[i]);
				headerObj.setTemplateMeta(Arrays.asList(setTemplateObj(templateIdsExpected.split(",")[i], tempstatus)));
				headerObj.setStatus(headerStatus);
				headerObjList.add(headerObj);

			}

			dltInfo.setPeId(peId);
			dltInfo.setHeaderInfo(headerObjList);

			// smsTempObj.setTemplateType("PROMOTIONAL");

			obj.setDltInfo(dltInfo);

		} else {

			DLTInfoDTO profileObj = new DLTInfoDTO();
			DLTContractInfoDTO loa = new DLTContractInfoDTO();

			profileObj.setCompanyName(companyName);
			profileObj.setCompanyType(type);
			profileObj.setEmailId(order.getEmailIdAd());
			profileObj.setGstn(order.getGstinAd());
			profileObj.setMobileNumber(order.getMobileNumberAd());
			profileObj.setName(order.getFirstName() + order.getLastName());
			profileObj.setPan(pan);

			loa.setAccepted(contractFlag);
			
			for (DocumentType doctype : docTypesList) {
				DocumentEntity document = setdocumentOb(doctype);
				profileObj.setAnyDocument(document, doctype);
			}


			obj.setLoa(loa);
			obj.setRegistrationDetails(profileObj);
		}

		if (approve) {

			for (int i = 0; i < 4; i++) {

				DLTMetaInfoDTO.SMSHeaderDto headerObj = new SMSHeaderDto();

				headerObj.setHeaderId(headerIdsExpected.split(",")[i]);
				headerObj.setTemplateMeta(Arrays.asList(setTemplateObj(templateIdsExpected.split(",")[i], tempstatus)));
				headerObj.setStatus(headerStatus);
				headerObjList.add(headerObj);

			}

			dltInfo.setPeId(peId);
			dltInfo.setHeaderInfo(headerObjList);

			// smsTempObj.setTemplateType("PROMOTIONAL");

			obj.setDltInfo(dltInfo);

		}

		obj.setStatus(status);

		return obj;

	}

	public SMSTemplateDto setTemplateObj(String id, TemplateStatus tempstatus) {

		SMSTemplateDto tempObj = new SMSTemplateDto();
		tempObj.setId(id);
		tempObj.setContent(templateURL);
		tempObj.setStatus(tempstatus);

		return tempObj;

	}


	public void registerDLtwithPan(OrderGlobalVariables variables,  boolean acceptedFlag,UserCompanyResponseVo userCompanyResponse,String uniqueIdentifier) {
		
		smsadaptor.registerDLTwithPan(variables.getCompanyUuid(), variables.getUserUUid(), userCompanyResponse,variables.getPanPathName(), OrganisationType.GOVERNMENT, acceptedFlag, 200, uniqueIdentifier);
		smsadaptor.fetchContract(variables.getCompanyUuid(), variables.getUserUUid(), "LOA",uniqueIdentifier);
		smsMongo.updateEmailMobile(variables.getCompanyUuid(), true);
		smsadaptor.registerDLTwithPan(variables.getCompanyUuid(), variables.getUserUUid(),userCompanyResponse, variables.getPanPathName(), OrganisationType.GOVERNMENT, acceptedFlag, 200,uniqueIdentifier);
	}

}



