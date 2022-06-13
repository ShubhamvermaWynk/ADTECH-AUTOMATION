package com.airtel.adtech.dto.response.sms;

import java.math.BigDecimal;
import java.util.List;

import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.entity.CampaignDTO.Template;
import com.airtel.adtech.entity.CampaignDuration;
import com.airtel.adtech.entity.CampaignMetaData;
import com.airtel.adtech.entity.GeoLocation;
import com.airtel.adtech.entity.Industry;

public class FetchCampaignDTO {
	
	private CampaignFetchDTO campaign;
	private DltFetchDTO dlt;
	
	
	
	
	
	


	public static class DltFetchDTO{
		public String getPeId() {
			return peId;
		}
		public void setPeId(String peId) {
			this.peId = peId;
		}
		public String getHeaderId() {
			return headerId;
		}
		public void setHeaderId(String headerId) {
			this.headerId = headerId;
		}
		private String peId;
		private String headerId;

	}

	
	public static class CampaignFetchDTO{
		
		private String id;
		private CampaignStatus status;
		private String title;
		private Template templateDetails;
		private CampaignDuration campaignDuration;
		private TargetingFetchDTO targeting;
		public BigDecimal budget;
		private CampaignMetaData campaignMetadata;
		
		
		
		
		
		public CampaignMetaData getCampaignMetadata() {
			return campaignMetadata;
		}
		public void setCampaignMetadata(CampaignMetaData campaignMetadata) {
			this.campaignMetadata = campaignMetadata;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public CampaignStatus getStatus() {
			return status;
		}
		public void setStatus(CampaignStatus status) {
			this.status = status;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Template getTemplateDetails() {
			return templateDetails;
		}
		public void setTemplateDetails(Template templateDetails) {
			this.templateDetails = templateDetails;
		}
		public CampaignDuration getCampaignDuration() {
			return campaignDuration;
		}
		public void setCampaignDuration(CampaignDuration campaignDuration) {
			this.campaignDuration = campaignDuration;
		}
		public TargetingFetchDTO getTargeting() {
			return targeting;
		}
		public void setTargeting(TargetingFetchDTO targeting) {
			this.targeting = targeting;
		}
		public BigDecimal getBudget() {
			return budget;
		}
		public void setBudget(BigDecimal budget) {
			this.budget = budget;
		}
		
		
		
		
	}
	
	public static class TargetingFetchDTO {
		
		public List<Industry> industry;
		public GeoLocation geo;
		public Long audienceReachCount;
		
		public List<Industry> getIndustry() {
			return industry;
		}
		public void setIndustry(List<Industry> industry) {
			this.industry = industry;
		}
		public GeoLocation getGeo() {
			return geo;
		}
		public void setGeo(GeoLocation geo) {
			this.geo = geo;
		}
		public Long getAudienceReachCount() {
			return audienceReachCount;
		}
		public void setAudienceReachCount(Long audienceReachCount) {
			this.audienceReachCount = audienceReachCount;
		}
		
		
		
	}
	

	public CampaignFetchDTO getCampaign() {
		return campaign;
	}


	public void setCampaign(CampaignFetchDTO campaign) {
		this.campaign = campaign;
	}


	public DltFetchDTO getDlt() {
		return dlt;
	}


	public void setDlt(DltFetchDTO dlt) {
		this.dlt = dlt;
	}


	@Override
	public String toString() {
		return "FetchCampaignDTO [campaign=" + campaign + ", dlt=" + dlt + "]";
	}


	

	



	
	
	
}
