package com.airtel.adtech.dto.response.sms;

import java.time.Instant;

public class AdminCampaignListDTO {
	
	private String campaignId;
	private Instant executionDate;
	private String slotTime;
	private Instant createdAt;
	private String campaignName;
	private Instant updatedAt;
	private String companyName;
	
	
	
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public Instant getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Instant executionDate) {
		this.executionDate = executionDate;
	}
	public String getSlotTime() {
		return slotTime;
	}
	public void setSlotTime(String slotTime) {
		this.slotTime = slotTime;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "AdminCampaignListDTO [campaignId=" + campaignId + ", executionDate=" + executionDate + ", slotTime="
				+ slotTime + ", createdAt=" + createdAt + ", campaignName=" + campaignName + ", updatedAt=" + updatedAt
				+ ", companyName=" + companyName + "]";
	}
	
	
	

}
