package com.airtel.adtech.dto.response.sms;

import com.airtel.adtech.dto.mongo.PartnerSMSProfileDTO;

public class AdminTagPeidDTO {
	

private PartnerSMSProfileDTO partnerSMSProfileDto;
private boolean isCampaignCreated;

public PartnerSMSProfileDTO getPartnerSMSProfileDto() {
	return partnerSMSProfileDto;
}
public void setPartnerSMSProfileDto(PartnerSMSProfileDTO partnerSMSProfileDto) {
	this.partnerSMSProfileDto = partnerSMSProfileDto;
}
public boolean isCampaignCreated() {
	return isCampaignCreated;
}
public void setCampaignCreated(boolean isCampaignCreated) {
	this.isCampaignCreated = isCampaignCreated;
}
@Override
public String toString() {
	return "AdminPartnerDetailsDTO [partnerSMSProfileDto=" + partnerSMSProfileDto + ", isCampaignCreated="
			+ isCampaignCreated + "]";
}




}
