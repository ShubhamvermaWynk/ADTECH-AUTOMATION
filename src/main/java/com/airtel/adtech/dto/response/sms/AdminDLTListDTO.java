package com.airtel.adtech.dto.response.sms;

import java.time.Instant;

import com.airtel.adtech.constants.enums.DLTProfileStatus;

public class AdminDLTListDTO {
	
	private String companyUuid;
	private String contactName;
	private String email;
	private String companyName;
	private Instant submissionDate;
	private Instant createdAt;
	private Instant updatedAt;
	private DLTProfileStatus status;
	private String peId;
	
	
	
	public String getCompanyUuid() {
		return companyUuid;
	}
	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
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
	
	public DLTProfileStatus getStatus() {
		return status;
	}
	public void setStatus(DLTProfileStatus status) {
		this.status = status;
	}
	public String getPeId() {
		return peId;
	}
	public void setPeId(String peId) {
		this.peId = peId;
	}
	@Override
	public String toString() {
		return " companyUuid=" + companyUuid + ", contactName=" + contactName + ", email=" + email
				+ ", companyName=" + companyName + ", submissionDate=" + submissionDate + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", status=" + status + ", peId=" + peId + "";
	}
	
	
	
	

}
