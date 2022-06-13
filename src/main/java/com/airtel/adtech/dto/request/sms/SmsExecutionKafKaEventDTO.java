package com.airtel.adtech.dto.request.sms;


import java.time.Instant;
import java.util.List;

import com.airtel.adtech.constants.enums.CampaignExecutionStatus;

public class SmsExecutionKafKaEventDTO {
	
	private String campaignId;
	private CampaignExecutionStatus status;
	private List<String> shortcodes;
	private Instant startTime;
	private Instant endTime;
	
	
	
	
	public List<String> getShortcodes() {
		return shortcodes;
	}
	public void setShortcodes(List<String> shortcodes) {
		this.shortcodes = shortcodes;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public CampaignExecutionStatus getStatus() {
		return status;
	}
	public void setStatus(CampaignExecutionStatus status) {
		this.status = status;
	}

	public Instant getStartTime() {
		return startTime;
	}
	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}
	public Instant getEndTime() {
		return endTime;
	}
	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "SmsExecutionKafKaEventDTO [campaignId=" + campaignId + ", status=" + status + ", shortcodes="
				+ shortcodes + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
	
	
	
	

}
