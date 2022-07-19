package com.airtel.adtech.dto.mongo;

public class ProfileMongoInfoDTO {
	
	private String contactName;
	private String email;
	private String mobileNumber;
	private boolean twoFaVerified;
	private boolean mobileVerified;
	private boolean emailVerified;
	
	
	
	
	
	
	public boolean isMobileVerified() {
		return mobileVerified;
	}
	public void setMobileVerified(boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public boolean isTwoFaVerified() {
		return twoFaVerified;
	}
	public void setTwoFaVerified(boolean twoFaVerified) {
		this.twoFaVerified = twoFaVerified;
	}
	
	
	

}
