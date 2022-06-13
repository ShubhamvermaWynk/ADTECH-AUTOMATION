package com.airtel.common.dto.response;

import com.airtel.common.constants.enums.CompanyType;

import java.util.List;
import java.util.Objects;

public class CompanyDTO {

	private String name;
	private CompanyType type;
	private String natureOfBusiness;
	private String uuid;
	private String panNumber;
	private String companyType;
	private Boolean archived;
	private String code;
	private List<AddressDTO> addresses;
	private List<CompanyKycDTO> companyKycs;
	private List<CompanyBankDetailDTO> bankDetails;
	private String description;
	private CategoryBusinessDTO categoryOfBusiness;
	private String tradeName;

	public String getName() {
		return name;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompanyType getType() {
		return type;
	}

	public void setType(CompanyType type) {
		this.type = type;
	}

	public String getNatureOfBusiness() {
		return natureOfBusiness;
	}

	public void setNatureOfBusiness(String natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public List<CompanyKycDTO> getCompanyKycs() {
		return companyKycs;
	}

	public void setCompanyKycs(List<CompanyKycDTO> companyKycs) {
		this.companyKycs = companyKycs;
	}

	public List<CompanyBankDetailDTO> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<CompanyBankDetailDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryBusinessDTO getCategoryOfBusiness() {
		return categoryOfBusiness;
	}

	public void setCategoryOfBusiness(CategoryBusinessDTO categoryOfBusiness) {
		this.categoryOfBusiness = categoryOfBusiness;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CompanyDTO)) return false;
		CompanyDTO that = (CompanyDTO) o;
		return Objects.equals(getName(), that.getName()) &&
				getType() == that.getType() &&
				Objects.equals(getNatureOfBusiness(), that.getNatureOfBusiness()) &&
				Objects.equals(getUuid(), that.getUuid()) &&
				Objects.equals(getPanNumber(), that.getPanNumber()) &&
				Objects.equals(getCompanyType(), that.getCompanyType()) &&
				Objects.equals(getArchived(), that.getArchived()) &&
				Objects.equals(getCode(), that.getCode()) &&
				Objects.equals(getAddresses(), that.getAddresses()) &&
				Objects.equals(getCompanyKycs(), that.getCompanyKycs()) &&
				Objects.equals(getBankDetails(), that.getBankDetails()) &&
				Objects.equals(getDescription(), that.getDescription()) &&
				Objects.equals(getCategoryOfBusiness(), that.getCategoryOfBusiness());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getType(), getNatureOfBusiness(), getUuid(), getPanNumber(), getCompanyType(), getArchived(), getCode(), getAddresses(), getCompanyKycs(), getBankDetails(), getDescription(), getCategoryOfBusiness());
	}

	@Override
	public String toString() {
		return "CompanyDTO [name=" + name + ", type=" + type + ", natureOfBusiness=" + natureOfBusiness + ", uuid="
				+ uuid + ", panNumber=" + panNumber + ", companyType=" + companyType + ", archived=" + archived
				+ ", code=" + code + ", addresses=" + addresses + ", companyKycs=" + companyKycs + ", bankDetails="
				+ bankDetails + ", description=" + description + ", categoryOfBusiness=" + categoryOfBusiness
				+ ", tradeName=" + tradeName + "]";
	}
}
