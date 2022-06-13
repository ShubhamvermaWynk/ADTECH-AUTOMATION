package com.airtel.adtech.dto.request.sms;

public class AdminCampaignApprovalDTO {
	
	private String campaignId;
	
	private String templateId;
	
	private String template;

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	
	

}
