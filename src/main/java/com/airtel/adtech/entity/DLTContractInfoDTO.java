package com.airtel.adtech.entity;

public class DLTContractInfoDTO {

	private boolean mobileVerified;
	private boolean emailVerified;
	private boolean accepted;
	
	
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
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	
	
	
}
