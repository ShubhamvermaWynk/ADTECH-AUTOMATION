package com.airtel.adtech.dto.mongo;

import com.airtel.adtech.constants.enums.AudienceStatus;
import com.airtel.adtech.dto.request.sms.SubmitAudiencetargeting;

public class AudienceMongoDTO {
	
	private String _id;
	private SubmitAudiencetargeting audienceTargetting;
	private String filePath;
	private Long estimatedReach;
	private AudienceStatus status;
	
	
	
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public SubmitAudiencetargeting getAudienceTargetting() {
		return audienceTargetting;
	}
	public void setAudienceTargetting(SubmitAudiencetargeting audienceTargetting) {
		this.audienceTargetting = audienceTargetting;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getEstimatedReach() {
		return estimatedReach;
	}
	public void setEstimatedReach(Long estimatedReach) {
		this.estimatedReach = estimatedReach;
	}
	public AudienceStatus getStatus() {
		return status;
	}
	public void setStatus(AudienceStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AudienceMongoDTO [_id=" + _id + ", audienceTargetting=" + audienceTargetting + ", filePath=" + filePath
				+ ", estimatedReach=" + estimatedReach + ", status=" + status + "]";
	}
	
	
	
	
	

}
