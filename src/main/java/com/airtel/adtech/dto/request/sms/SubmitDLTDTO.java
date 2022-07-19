package com.airtel.adtech.dto.request.sms;

import java.io.File;

import com.airtel.adtech.constants.enums.OrganisationType;

public class SubmitDLTDTO {

	
	private String name;
	private String emailId;
	private String mobileNumber;
	private String companyName;
	private OrganisationType organisationType;
	private String pan;
	private String gstn;
	private File panDocument;

	private File loaDocument;
	private boolean accepted;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public OrganisationType getOrganisationType() {
		return organisationType;
	}
	public void setOrganisationType(OrganisationType organisationType) {
		this.organisationType = organisationType;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getGstn() {
		return gstn;
	}
	public void setGstn(String gstn) {
		this.gstn = gstn;
	}
	public File getPanDocument() {
		return panDocument;
	}
	public void setPanDocument(File panDocument) {
		this.panDocument = panDocument;
	}
	public File getLoaDocument() {
		return loaDocument;
	}
	public void setLoaDocument(File loaDocument) {
		this.loaDocument = loaDocument;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	
	
	
	
	
}
