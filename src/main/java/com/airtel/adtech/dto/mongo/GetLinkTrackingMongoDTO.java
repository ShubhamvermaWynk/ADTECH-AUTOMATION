package com.airtel.adtech.dto.mongo;

import java.time.Instant;
import java.util.List;

import com.airtel.adtech.entity.Validity;

public class GetLinkTrackingMongoDTO {
	
	public String campaignId;
	public String shortCode;
	public Instant urlCreationDate;
	public Validity validity;
	public String createdAt;
	public String updatedAt;
	public List<SmsCampaignLinkStatistics> linkStatistics;





public static class SmsCampaignLinkStatistics{
	
	public Integer linkClickedCount;
	public Integer retryCount;
	public String status;
	public Instant createdAt;
	public Instant updatedAt;
	public Instant queryInstant;
	
	
	
	
	public Integer getLinkClickedCount() {
		return linkClickedCount;
	}
	public void setLinkClickedCount(Integer linkClickedCount) {
		this.linkClickedCount = linkClickedCount;
	}
	public Instant getQueryInstant() {
		return queryInstant;
	}
	public void setQueryInstant(Instant queryInstant) {
		this.queryInstant = queryInstant;
	}
	public Integer getLinkClicked() {
		return linkClickedCount;
	}
	public void setLinkClicked(Integer linkClicked) {
		this.linkClickedCount = linkClicked;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
		return "SmsCampaignLinkStatistics [linkClickedCount=" + linkClickedCount + ", retryCount=" + retryCount
				+ ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", queryInstant="
				+ queryInstant + "]";
	}
	
	
	
}

public String getCampaignId() {
	return campaignId;
}

public void setCampaignId(String campaignId) {
	this.campaignId = campaignId;
}

public String getShortCode() {
	return shortCode;
}

public void setShortCode(String shortCode) {
	this.shortCode = shortCode;
}



public Instant getUrlCreationDate() {
	return urlCreationDate;
}

public void setUrlCreationDate(Instant urlCreationDate) {
	this.urlCreationDate = urlCreationDate;
}

public Validity getValidity() {
	return validity;
}

public void setValidity(Validity validity) {
	this.validity = validity;
}

public String getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
}

public String getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
	this.updatedAt = updatedAt;
}


public List<SmsCampaignLinkStatistics> getLinkStatistics() {
	return linkStatistics;
}

public void setLinkStatistics(List<SmsCampaignLinkStatistics> linkStatistics) {
	this.linkStatistics = linkStatistics;
}

@Override
public String toString() {
	return "GetLinkTrackingMongoDTO [campaignId=" + campaignId + ", shortCode=" + shortCode + ", urlCreationDate="
			+ urlCreationDate + ", validity=" + validity + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
			+ ", smsCampaignLinkStatistics=" + linkStatistics + "]";
}




}


