package com.airtel.adtech.dto.mongo;

import java.time.Instant;
import java.util.List;

import com.airtel.adtech.constants.enums.TemplateStatus;

public class DLTMetaInfoMongoDTO {
	private String peId;
	private List<SMSHeaderMongoDto> headerInfo;
	private Instant peIdTaggedDate;

	
	public static class SMSHeaderMongoDto {

		private String headerId;
		private String status;
		private List<SMSTemplateMongoDto> templateMeta;
		
		
		
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
		public List<SMSTemplateMongoDto> getTemplateMeta() {
			return templateMeta;
		}
		public void setTemplateMeta(List<SMSTemplateMongoDto> templateMeta) {
			this.templateMeta = templateMeta;
		}
		
		

	}	

	
	public static class SMSTemplateMongoDto{
		
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
	





	public Instant getPeIdTaggedDate() {
		return peIdTaggedDate;
	}


	public void setPeIdTaggedDate(Instant peIdTaggedDate) {
		this.peIdTaggedDate = peIdTaggedDate;
	}


	public String getPeId() {
		return peId;
	}


	public void setPeId(String peId) {
		this.peId = peId;
	}


	public List<SMSHeaderMongoDto> getHeaderInfo() {
		return headerInfo;
	}


	public void setHeaderInfo(List<SMSHeaderMongoDto> headerInfo) {
		this.headerInfo = headerInfo;
	}


	@Override
	public String toString() {
		return "DLTMetaInfoMongoDTO [peId=" + peId + ", headerInfo=" + headerInfo + ", peIdTaggedDate=" + peIdTaggedDate
				+ "]";
	}


	

}
