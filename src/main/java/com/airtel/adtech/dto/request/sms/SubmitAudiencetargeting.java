package com.airtel.adtech.dto.request.sms;

import com.airtel.adtech.entity.Industry;

import java.util.List;

import com.airtel.adtech.entity.GeoLocation;

public class SubmitAudiencetargeting {
	
	private GeoLocation geoLocation;
	
	private List<Industry> industry;
	
	
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public List<Industry> getIndustry() {
		return industry;
	}

	public void setIndustry(List<Industry> industry) {
		this.industry = industry;
	}

	@Override
	public String toString() {
		return "SubmitAudiencetargeting [geoLocation=" + geoLocation + ", industry=" + industry + "]";
	}
	
	
	
	

}
