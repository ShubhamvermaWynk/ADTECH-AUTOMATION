package com.airtel.adtech.dto.request.sms;

import java.util.List;

import com.airtel.adtech.entity.Industry;

public class SubmitAudienceWithRadiusDTO {

	
	
	private Geo geo;
	
	private List<String> industry;
	
	private Radius radius;

	
	
	public static class Geo{
		
		private Double latitude;
		private Double longitude;
			
		public Double getLatitude() {
			return latitude;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}

		
		
		
		
	}
	
	
	

	public Geo getGeoLocation() {
		return geo;
	}

	public void setGeoLocation(Geo geo) {
		this.geo = geo;
	}

	public static class Radius{
		
		private Long value;
		private String unit;
		
		public Long getValue() {
			return value;
		}

		public void setValue(Long value) {
			this.value = value;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		
	}
	
	
	
	
	
	public Radius getRadius() {
		return radius;
	}

	public void setRadius(Radius radius) {
		this.radius = radius;
	}

	
	public List<String> getIndustry() {
		return industry;
	}

	public void setIndustry(List<String> industry) {
		this.industry = industry;
	}
	
	
	
}
