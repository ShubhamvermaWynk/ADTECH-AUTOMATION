package com.airtel.common.dto.response;

import com.airtel.common.constants.enums.CompanyKycType;

import java.util.Date;

public class CompanyKycDTO {

	private CompanyKycType kycDocumentType;
	private String kycStatus;

	private String value;

	private String url;

	private Boolean verified;

	private Boolean active;

	private Date verifiedAt;

	
	
	
	
	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public CompanyKycType getKycDocumentType() {
		return kycDocumentType;
	}

	public void setKycDocumentType(CompanyKycType kycDocumentType) {
		this.kycDocumentType = kycDocumentType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(Date verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CompanyKycDTO)) return false;

		CompanyKycDTO that = (CompanyKycDTO) o;

		if (getKycDocumentType() != that.getKycDocumentType()) return false;
		if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
		if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) return false;
		if (getVerified() != null ? !getVerified().equals(that.getVerified()) : that.getVerified() != null)
			return false;
		if (getActive() != null ? !getActive().equals(that.getActive()) : that.getActive() != null) return false;
		return getVerifiedAt() != null ? getVerifiedAt().equals(that.getVerifiedAt()) : that.getVerifiedAt() == null;
	}

	@Override
	public int hashCode() {
		int result = getKycDocumentType() != null ? getKycDocumentType().hashCode() : 0;
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
		result = 31 * result + (getVerified() != null ? getVerified().hashCode() : 0);
		result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
		result = 31 * result + (getVerifiedAt() != null ? getVerifiedAt().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CompanyKycDTO{" +
				"kycDocumentType=" + kycDocumentType +
				", value='" + value + '\'' +
				", url='" + url + '\'' +
				", verified=" + verified +
				", active=" + active +
				", verifiedAt=" + verifiedAt +
				'}';
	}
}
