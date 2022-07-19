package com.airtel.adtech.entity;

public class GeoLocation {
	private Double latitude;
	private Double longitude;
	private long coverage;
	private  String locationAddress;
	
	
	
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public long getCoverage() {
		return coverage;
	}
	public void setCoverage(long coverage) {
		this.coverage = coverage;
	}
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
	@Override
	public String toString() {
		return "GeoLocation [latitude=" + latitude + ", longitude=" + longitude + ", coverage=" + coverage
				+ ", locationAddress=" + locationAddress + "]";
	}
	
	
	

}
