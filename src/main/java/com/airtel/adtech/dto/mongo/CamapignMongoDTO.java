package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.airtel.adtech.constants.enums.CampaignStatus;
import com.airtel.adtech.entity.CampaignDuration;
import com.airtel.adtech.entity.CampaignMetaData;
import com.airtel.adtech.entity.PartnerSmsProfile;

public class CamapignMongoDTO {
	
	private String id;
	private String type;
	private String campaignId;
	private String companyUuid;
	private String userUuid;	
	private CampaignStatus status;
	private PartnerSmsProfile partnerSmsProfile;
	private String title;
	private String templateSmsContent;
	private CampaignDuration campaignDuration;
	private String audience;
	private List<String> lastUpdatedBy;
	private BigDecimal estimatedBudget;
	private Instant createdAt;
	private Instant updatedAt;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
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
	public CampaignStatus getStatus() {
		return status;
	}
	public void setStatus(CampaignStatus status) {
		this.status = status;
	}
	public PartnerSmsProfile getPartnerSmsProfile() {
		return partnerSmsProfile;
	}
	public void setPartnerSmsProfile(PartnerSmsProfile partnerSmsProfile) {
		this.partnerSmsProfile = partnerSmsProfile;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTemplateSmsContent() {
		return templateSmsContent;
	}
	public void setTemplateSmsContent(String templateSmsContent) {
		this.templateSmsContent = templateSmsContent;
	}
	public CampaignDuration getCampaignDuration() {
		return campaignDuration;
	}
	public void setCampaignDuration(CampaignDuration campaignDuration) {
		this.campaignDuration = campaignDuration;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public List<String> getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(List<String> lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public BigDecimal getEstimatedBudget() {
		return estimatedBudget;
	}
	public void setEstimatedBudget(BigDecimal estimatedBudget) {
		this.estimatedBudget = estimatedBudget;
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
		return "CamapignMongoDTO [id=" + id + ", type=" + type + ", campaignId=" + campaignId + ", companyUuid="
				+ companyUuid + ", userUuid=" + userUuid + ", status=" + status + ", partnerSmsProfile="
				+ partnerSmsProfile + ", title=" + title + ", templateSmsContent=" + templateSmsContent
				+ ", campaignDuration=" + campaignDuration + ", audience=" + audience + ", lastUpdatedBy="
				+ lastUpdatedBy + ", estimatedBudget=" + estimatedBudget + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", campaignMetadata=" + campaignMetadata + "]";
	}
	
	
	

}
