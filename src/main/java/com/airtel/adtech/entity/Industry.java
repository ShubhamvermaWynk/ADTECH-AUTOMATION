package com.airtel.adtech.entity;

public class Industry {
	
	private String key;
	private String displayName;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
	public String toString() {
		return "Industry [key=" + key + ", displayName=" + displayName + "]";
	}
	
	

	
	
}
