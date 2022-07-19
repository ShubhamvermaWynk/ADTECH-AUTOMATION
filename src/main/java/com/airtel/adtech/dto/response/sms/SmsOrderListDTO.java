package com.airtel.adtech.dto.response.sms;

import java.util.List;

public class SmsOrderListDTO {
	
	private List<SmsOrderDTO> PENDING_WITH_USER;
	private List<SmsOrderDTO> PENDING_WITH_AIRTEL;
	private List<SmsOrderDTO> CONFIRMED;
	
	
	public List<SmsOrderDTO> getPENDING_WITH_USER() {
		return PENDING_WITH_USER;
	}
	public void setPENDING_WITH_USER(List<SmsOrderDTO> pENDING_WITH_USER) {
		PENDING_WITH_USER = pENDING_WITH_USER;
	}
	public List<SmsOrderDTO> getPENDING_WITH_AIRTEL() {
		return PENDING_WITH_AIRTEL;
	}
	public void setPENDING_WITH_AIRTEL(List<SmsOrderDTO> pENDING_WITH_AIRTEL) {
		PENDING_WITH_AIRTEL = pENDING_WITH_AIRTEL;
	}
	public List<SmsOrderDTO> getCONFIRMED() {
		return CONFIRMED;
	}
	public void setCONFIRMED(List<SmsOrderDTO> cONFIRMED) {
		CONFIRMED = cONFIRMED;
	}
	@Override
	public String toString() {
		return "SmsOrderListDTO [PENDING_WITH_USER=" + PENDING_WITH_USER + ", PENDING_WITH_AIRTEL="
				+ PENDING_WITH_AIRTEL + ", CONFIRMED=" + CONFIRMED + "]";
	}

	
	
	
}
