package com.airtel.adtech.entity;

import java.math.BigDecimal;

import com.airtel.adtech.constants.enums.CampaignStatus;

public class CampaignDTO {
	
	private String id;
	private CampaignStatus status;
	private String title;
	private Template templateDetails;
	private CampaignDuration campaignDuration;
	private TargetingDTO targeting;
	public BigDecimal budget;
	private CampaignMetaData campaignMetadata;
	
	
	
	
	public CampaignMetaData getCampaignMetadata() {
		return campaignMetadata;
	}


	public void setCampaignMetadata(CampaignMetaData campaignMetadata) {
		this.campaignMetadata = campaignMetadata;
	}


	public static class Template {
		
		private String id;
		private String content;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
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


	public TargetingDTO getTargeting() {
		return targeting;
	}


	public void setTargeting(TargetingDTO targeting) {
		this.targeting = targeting;
	}


	public BigDecimal getBudget() {
		return budget;
	}


	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	
	
	

}
