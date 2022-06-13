package com.airtel.adtech.entity;

import com.airtel.adtech.constants.enums.DocumentType;

public class DocumentEntity {
	private String name;
	private long size;
	private String documentKey;
	private DocumentType documentType;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDocumentKey() {
		return documentKey;
	}
	public void setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
	}
	public DocumentType getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
}
