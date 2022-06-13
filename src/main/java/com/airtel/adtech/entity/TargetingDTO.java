package com.airtel.adtech.entity;

import java.util.List;

public class TargetingDTO {
	
	public List<String> industry;
	public GeoLocation geo;
	public Long audienceReachCount;
	
	
	
	
	public List<String> getIndustry() {
		return industry;
	}
	public void setIndustry(List<String> industry) {
		this.industry = industry;
	}
	public GeoLocation getGeo() {
		return geo;
	}
	public void setGeo(GeoLocation geo) {
		this.geo = geo;
	}
	public Long getAudienceReachCount() {
		return audienceReachCount;
	}
	public void setAudienceReachCount(Long audienceReachCount) {
		this.audienceReachCount = audienceReachCount;
	}
	
	
	

}
