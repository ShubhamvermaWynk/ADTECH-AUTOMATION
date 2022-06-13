package com.airtel.adtech.dto.response.sms;

import java.time.Instant;

import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.entity.DLTContractInfoDTO;
import com.airtel.adtech.entity.DLTInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO;

public class AdminPartnerDetailsDTO {
	
	private DLTInfoDTO registrationDetails;

	private DLTContractInfoDTO loa;

	private DLTProfileStatus status;
	
	private DLTMetaInfoDTO dltInfo;
	
	private boolean isCampaignSubmitted;
	
	private String companyUuid;
	private String userUuid;

	private Instant submissionDate;

	public DLTInfoDTO getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(DLTInfoDTO registrationDetails) {
		this.registrationDetails = registrationDetails;
	}

	public DLTContractInfoDTO getLoa() {
		return loa;
	}

	public void setLoa(DLTContractInfoDTO loa) {
		this.loa = loa;
	}

	public DLTProfileStatus getStatus() {
		return status;
	}

	public void setStatus(DLTProfileStatus status) {
		this.status = status;
	}



	public boolean isCampaignSubmitted() {
		return isCampaignSubmitted;
	}

	public void setCampaignSubmitted(boolean isCampaignSubmitted) {
		this.isCampaignSubmitted = isCampaignSubmitted;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public Instant getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Instant submissionDate) {
		this.submissionDate = submissionDate;
	}

	public DLTMetaInfoDTO getDltInfo() {
		return dltInfo;
	}

	public void setDltInfo(DLTMetaInfoDTO dltInfo) {
		this.dltInfo = dltInfo;
	}
	
	

}
