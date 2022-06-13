package com.airtel.adtech.dto.mongo;

import com.airtel.adtech.constants.enums.TemplateStatus;

public class SMSTemplateMongoDto {
	
	private String _id;
	private String content;
	private TemplateStatus status;
	private String templateType;
	
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public TemplateStatus getStatus() {
		return status;
	}
	public void setStatus(TemplateStatus status) {
		this.status = status;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	
	
	
}
