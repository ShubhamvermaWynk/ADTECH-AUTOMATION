package com.airtel.adtech.dto.mongo;

import java.time.Instant;

import com.airtel.adtech.constants.enums.DLTProfileStatus;
import com.airtel.adtech.entity.DLTInfoDTO;
import com.airtel.adtech.entity.DLTMetaInfoDTO;

public class PartnerSMSProfileDTO {
	
	private String companyUuid;
	private String userUuid;
	private DLTProfileStatus status;

	private boolean alreadyRegistered;

	private ProfileMongoInfoDTO profile;

	private CompanyMongoDTO companyDetails;

	private ContractMongoDTO contract;

	private DLTMetaInfoMongoDTO dltInfo;
	
	
	private Instant createdAt;
	private Instant updatedAt;
	private Instant submissionDate;
	
	
	
	public Instant getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Instant submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
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

	public DLTProfileStatus getStatus() {
		return status;
	}

	public void setStatus(DLTProfileStatus status) {
		this.status = status;
	}

	public boolean isAlreadyRegistered() {
		return alreadyRegistered;
	}

	public void setAlreadyRegistered(boolean alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	public ProfileMongoInfoDTO getProfile() {
		return profile;
	}

	public void setProfile(ProfileMongoInfoDTO profile) {
		this.profile = profile;
	}

	public CompanyMongoDTO getCompanyDetails() {
		return companyDetails;
	}

	public void setCompanyDetails(CompanyMongoDTO companyDetails) {
		this.companyDetails = companyDetails;
	}

	public ContractMongoDTO getContract() {
		return contract;
	}

	public void setContract(ContractMongoDTO contract) {
		this.contract = contract;
	}

	public DLTMetaInfoMongoDTO getDltInfo() {
		return dltInfo;
	}

	public void setDltInfo(DLTMetaInfoMongoDTO dltInfo) {
		this.dltInfo = dltInfo;
	}

	@Override
	public String toString() {
		return "PartnerSMSProfileDTO [companyUuid=" + companyUuid + ", userUuid=" + userUuid + ", status=" + status
				+ ", alreadyRegistered=" + alreadyRegistered + ", profile=" + profile + ", companyDetails="
				+ companyDetails + ", contract=" + contract + ", dltInfo=" + dltInfo + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", submissionDate=" + submissionDate + "]";
	}

	

	
	
	
	
	
	

}
