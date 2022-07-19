package com.airtel.adtech.entity;

import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.constants.enums.OrganisationType;

public class DLTInfoDTO {
	
	private String name;
	private String emailId;
	private String mobileNumber;
	private String companyName;
	private OrganisationType companyType;
	private String pan;
	private String gstn;
	private DocumentEntity panDocument;
	private DocumentEntity gstDocument;
	private DocumentEntity proofEntityDocument;
	private DocumentEntity proofIdentityDocument;
	
	
	
	public void setAnyDocument(DocumentEntity doc, DocumentType docType) {
		switch(docType) {
		case PAN: this.panDocument = doc; return;
		case GST: this.gstDocument = doc; return;
		case PROOF_ENTITY: this.proofEntityDocument = doc; return;
		case PROOF_IDENTITY: this.proofIdentityDocument = doc; return;
		default: return;
		}
	}

	
	
	public DocumentEntity getPanDocument() {
		return panDocument;
	}
	public void setPanDocument(DocumentEntity panDocument) {
		this.panDocument = panDocument;
	}
	public DocumentEntity getGstDocument() {
		return gstDocument;
	}
	public void setGstDocument(DocumentEntity gstDocument) {
		this.gstDocument = gstDocument;
	}
	public DocumentEntity getProofEntityDocument() {
		return proofEntityDocument;
	}
	public void setProofEntityDocument(DocumentEntity proofEntityDocument) {
		this.proofEntityDocument = proofEntityDocument;
	}
	public DocumentEntity getProofIdentityDocument() {
		return proofIdentityDocument;
	}
	public void setProofIdentityDocument(DocumentEntity proofIdentityDocument) {
		this.proofIdentityDocument = proofIdentityDocument;
	}
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
	
	
	
	public OrganisationType getCompanyType() {
		return companyType;
	}
	public void setCompanyType(OrganisationType companyType) {
		this.companyType = companyType;
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
	@Override
	public String toString() {
		return "DLTInfoDTO [name=" + name + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", companyName="
				+ companyName + ", companyType=" + companyType + ", pan=" + pan + ", gstn=" + gstn + "]";
	}
	
	
	

}
