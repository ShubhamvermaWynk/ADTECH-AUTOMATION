package com.airtel.adtech.dto.mongo;

import java.time.Instant;

import com.airtel.adtech.constants.enums.DocumentType;

public class ContractMongoDTO {
	
	private DocumentType type;
	private String documentKey;
	private boolean accepted;
	private String clientIp;
	private String userAgent;
	private String acceptedAt;
	private Instant createdAt;
	private Instant updatedAt;
	
	
	
	public DocumentType getType() {
		return type;
	}
	public void setType(DocumentType type) {
		this.type = type;
	}
	public String getDocumentKey() {
		return documentKey;
	}
	public void setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getAcceptedAt() {
		return acceptedAt;
	}
	public void setAcceptedAt(String acceptedAt) {
		this.acceptedAt = acceptedAt;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	

}
