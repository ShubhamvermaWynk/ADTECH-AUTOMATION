package com.airtel.common.dto.response;

import com.airtel.common.constants.enums.AccountType;

import java.util.Date;

public class CompanyBankDetailDTO {

	private Long id;
	private String beneficiaryName;
	private String accountNumber;
	private AccountType accountType;
	private String bankName;
	private String ifscCode;
	private String branchAddress;
	private String verificationStatus;
	private Boolean active;
	private Boolean verified;
	private Date verifiedAt;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Date getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(Date verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CompanyBankDetailDTO)) return false;

		CompanyBankDetailDTO that = (CompanyBankDetailDTO) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getBeneficiaryName() != null ? !getBeneficiaryName().equals(that.getBeneficiaryName()) : that.getBeneficiaryName() != null)
			return false;
		if (getAccountNumber() != null ? !getAccountNumber().equals(that.getAccountNumber()) : that.getAccountNumber() != null)
			return false;
		if (getAccountType() != that.getAccountType()) return false;
		if (getBankName() != null ? !getBankName().equals(that.getBankName()) : that.getBankName() != null)
			return false;
		if (getIfscCode() != null ? !getIfscCode().equals(that.getIfscCode()) : that.getIfscCode() != null)
			return false;
		if (getBranchAddress() != null ? !getBranchAddress().equals(that.getBranchAddress()) : that.getBranchAddress() != null)
			return false;
		if (getVerificationStatus() != null ? !getVerificationStatus().equals(that.getVerificationStatus()) : that.getVerificationStatus() != null)
			return false;
		if (getActive() != null ? !getActive().equals(that.getActive()) : that.getActive() != null) return false;
		if (getVerified() != null ? !getVerified().equals(that.getVerified()) : that.getVerified() != null)
			return false;
		if (getVerifiedAt() != null ? !getVerifiedAt().equals(that.getVerifiedAt()) : that.getVerifiedAt() != null)
			return false;
		return getStatus() != null ? getStatus().equals(that.getStatus()) : that.getStatus() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getBeneficiaryName() != null ? getBeneficiaryName().hashCode() : 0);
		result = 31 * result + (getAccountNumber() != null ? getAccountNumber().hashCode() : 0);
		result = 31 * result + (getAccountType() != null ? getAccountType().hashCode() : 0);
		result = 31 * result + (getBankName() != null ? getBankName().hashCode() : 0);
		result = 31 * result + (getIfscCode() != null ? getIfscCode().hashCode() : 0);
		result = 31 * result + (getBranchAddress() != null ? getBranchAddress().hashCode() : 0);
		result = 31 * result + (getVerificationStatus() != null ? getVerificationStatus().hashCode() : 0);
		result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
		result = 31 * result + (getVerified() != null ? getVerified().hashCode() : 0);
		result = 31 * result + (getVerifiedAt() != null ? getVerifiedAt().hashCode() : 0);
		result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CompanyBankDetailDTO{" +
				"id=" + id +
				", beneficiaryName='" + beneficiaryName + '\'' +
				", accountNumber='" + accountNumber + '\'' +
				", accountType=" + accountType +
				", bankName='" + bankName + '\'' +
				", ifscCode='" + ifscCode + '\'' +
				", branchAddress='" + branchAddress + '\'' +
				", verificationStatus='" + verificationStatus + '\'' +
				", active=" + active +
				", verified=" + verified +
				", verifiedAt=" + verifiedAt +
				", status='" + status + '\'' +
				'}';
	}
}
