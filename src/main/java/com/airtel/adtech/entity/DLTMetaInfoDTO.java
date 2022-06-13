package com.airtel.adtech.entity;

import java.util.List;

import com.airtel.adtech.constants.enums.TemplateStatus;
import com.airtel.adtech.dto.mongo.SMSTemplateMongoDto;

public class DLTMetaInfoDTO {
	
	
	private String peId;
	private List<SMSHeaderDto> headerInfo;

	
	public static class SMSHeaderDto {

		private String headerId;
		private String status;
		private List<SMSTemplateDto> templateMeta;
		
		
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getHeaderId() {
			return headerId;
		}
		public void setHeaderId(String headerId) {
			this.headerId = headerId;
		}
		public List<SMSTemplateDto> getTemplateMeta() {
			return templateMeta;
		}
		public void setTemplateMeta(List<SMSTemplateDto> templateMeta) {
			this.templateMeta = templateMeta;
		}
		
		

	}	
	
public static class SMSTemplateDto{
		
		private String id;
		private String content;
		private TemplateStatus status;
		private String templateType;
		
		
	
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
	

	
	


	public String getPeId() {
		return peId;
	}


	public void setPeId(String peId) {
		this.peId = peId;
	}


	public List<SMSHeaderDto> getHeaderInfo() {
		return headerInfo;
	}


	public void setHeaderInfo(List<SMSHeaderDto> headerInfo) {
		this.headerInfo = headerInfo;
	}

	
	

}
