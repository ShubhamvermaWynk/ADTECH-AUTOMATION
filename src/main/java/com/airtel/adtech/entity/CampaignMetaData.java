package com.airtel.adtech.entity;

import java.time.Instant;
import java.util.List;
import com.airtel.adtech.entity.Validity;


public class CampaignMetaData {
	
	private SmsLinkDetails smsLinkDetails;
	
	public SmsLinkDetails getSmsLinkDetails() {
		return smsLinkDetails;
	}

	public void setSmsLinkDetails(SmsLinkDetails smsLinkDetails) {
		this.smsLinkDetails = smsLinkDetails;
	}





	public static class SmsLinkDetails
	{
		private List<String> shortCode;
		private Instant urlCreationDate;
		private Validity validity;
		
		public List<String> getShortCode() {
			return shortCode;
		}
		public void setShortCode(List<String> shortCode) {
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
		@Override
		public String toString() {
			return "SmsLinkDetails [shortCode=" + shortCode + ", urlCreationDate=" + urlCreationDate + ", validity="
					+ validity + "]";
		}
		
		
		
		
	}





	@Override
	public String toString() {
		return "CampaignMetaData [smsLinkDetails=" + smsLinkDetails + "]";
	}
	
	

}
