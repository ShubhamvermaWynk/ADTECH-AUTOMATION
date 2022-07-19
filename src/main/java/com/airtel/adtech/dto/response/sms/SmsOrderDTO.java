package com.airtel.adtech.dto.response.sms;

import java.time.Instant;

import com.airtel.adtech.constants.enums.CampaignStatus;

public class SmsOrderDTO {
	
	private String campaignId;
	private CampaignStatus status;
	private Instant startDate;
	private String title;
	private Instant createdAt;
	private Instant updatedAt;
	
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public CampaignStatus getStatus() {
		return status;
	}
	public void setStatus(CampaignStatus status) {
		this.status = status;
	}
	public Instant getStartDate() {
		return startDate;
	}
	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	
	@Override
	public String toString() {
		return "SmsOrderDTO [campaignId=" + campaignId + ", status=" + status + ", startDate=" + startDate + ", title="
				+ title + ", createdAt=" + createdAt + "]";
	}
	
	

}
