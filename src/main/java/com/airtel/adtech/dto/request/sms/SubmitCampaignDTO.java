package com.airtel.adtech.dto.request.sms;

import com.airtel.adtech.entity.CampaignDTO;
import com.airtel.adtech.entity.CampaignMetaData;

public class SubmitCampaignDTO {
	
	
	private CampaignDTO campaign;
	private DltDTO dlt;
	
	
	public static class DltDTO{
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


	public CampaignDTO getCampaign() {
		return campaign;
	}


	public void setCampaign(CampaignDTO campaign) {
		this.campaign = campaign;
	}


	public DltDTO getDlt() {
		return dlt;
	}


	public void setDlt(DltDTO dlt) {
		this.dlt = dlt;
	}


	
	
	

}
