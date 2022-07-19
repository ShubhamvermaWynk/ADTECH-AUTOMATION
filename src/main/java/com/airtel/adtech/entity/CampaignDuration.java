package com.airtel.adtech.entity;

import java.time.Instant;

public class CampaignDuration {
	
	private Instant startDate;
	private String slotTime;
	private SlotBooking slotBooking;
	
	
	

	public String getSlotTime() {
		return slotTime;
	}

	public void setSlotTime(String slotTime) {
		this.slotTime = slotTime;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public SlotBooking getSlotBooking() {
		return slotBooking;
	}

	public void setSlotBooking(SlotBooking slotBooking) {
		this.slotBooking = slotBooking;
	}
	
	
	
	
	
	

}
