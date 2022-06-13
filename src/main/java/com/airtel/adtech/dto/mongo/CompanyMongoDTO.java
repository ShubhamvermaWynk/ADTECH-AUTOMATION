package com.airtel.adtech.dto.mongo;

import com.airtel.adtech.constants.enums.DocumentType;
import com.airtel.adtech.constants.enums.OrganisationType;

public class CompanyMongoDTO {
	
	private String companyName;
	private OrganisationType category;
	private String PAN;
	private String GSTN;

	private Document document;
	private Document gstDocument;
	private Document proofEntityDocument;
	private Document proofIdentityDocument;
	
	
	
	public static class Document{
		
		private DocumentType type;
		private String path;
		private String originalName;
		private int size; 
		
		
		

		public String getOriginalName() {
			return originalName;
		}

		public void setOriginalName(String originalName) {
			this.originalName = originalName;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public DocumentType getType() {
			return type;
		}

		public void setType(DocumentType type) {
			this.type = type;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return "Document [type=" + type + ", path=" + path + ", originalName=" + originalName + ", size=" + size
					+ "]";
		}

		

		
	}
	
	
	
	
	public Document getGstDocument() {
		return gstDocument;
	}
	public void setGstDocument(Document gstDocument) {
		this.gstDocument = gstDocument;
	}
	public Document getProofEntityDocument() {
		return proofEntityDocument;
	}
	public void setProofEntityDocument(Document proofEntityDocument) {
		this.proofEntityDocument = proofEntityDocument;
	}
	public Document getProofIdentityDocument() {
		return proofIdentityDocument;
	}
	public void setProofIdentityDocument(Document proofIdentityDocument) {
		this.proofIdentityDocument = proofIdentityDocument;
	}

	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public OrganisationType getCategory() {
		return category;
	}
	public void setCategory(OrganisationType category) {
		this.category = category;
	}
	
	
	
	
	
	public String getPAN() {
		return PAN;
	}
	public void setPAN(String PAN) {
		PAN = PAN;
	}
	public String getGSTN() {
		return GSTN;
	}
	public void setGSTN(String GSTN) {
		GSTN = GSTN;
	}
	
	
	
	
	@Override
	public String toString() {
		return "CompanyMongoDTO [companyName=" + companyName + ", category=" + category + ", PAN=" + PAN + ", GSTN="
				+ GSTN + ", document=" + document + ", gstDocument=" + gstDocument + ", proofEntityDocument="
				+ proofEntityDocument + ", proofIdentityDocument=" + proofIdentityDocument + "]";
	}
	
	
	public void setAnyDocument(Document doc, DocumentType docType) {
		switch(docType) {
		case PAN: this.document = doc; return;
		case GST: this.gstDocument = doc; return;
		case PROOF_ENTITY: this.proofEntityDocument = doc; return;
		case PROOF_IDENTITY: this.proofIdentityDocument = doc; return;
		default: return;
		}
	}

	
	
	
	
		

}
