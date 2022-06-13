package com.airtel.adtech.dto.response.sms;

import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.entity.DLTContractInfoDTO;
import com.airtel.adtech.entity.DLTInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FetchDLTInfoDTO {
	
	private DLTInfoDTO registrationDetails;

	private DLTContractInfoDTO loa;


	private DLTMetaInfoDTO dltInfo;
	
	private DLTProfileStatus status;
	
	
	

	public DLTProfileStatus getStatus() {
		return status;
	}

	public void setStatus(DLTProfileStatus status) {
		this.status = status;
	}

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

	
	public DLTMetaInfoDTO getDltInfo() {
		return dltInfo;
	}

	public void setDltInfo(DLTMetaInfoDTO dltInfo) {
		this.dltInfo = dltInfo;
	}

	@Override
	public String toString() {
		return "FetchDLTInfoDTO [registrationDetails=" + registrationDetails + ", loa=" + loa + ", dltInfo=" + dltInfo
				+ ", status=" + status + "]";
	}


	
	
	
}
