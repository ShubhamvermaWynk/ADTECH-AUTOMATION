package com.airtel.common.dto.response;

public class AddressDTO {

	private String buildingNumber;
	private Long id;
	private String line1;
	private String line2;
	private String line3;
	private String pincode;
	private String landmark;
	private String addressType;
	private String city;
	private String state;
	private String country;
	private Boolean active;
	private Boolean defaultAddr;
	private String description;
	private String district;
	private String gstinNumber;

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getDefaultAddr() {
		return defaultAddr;
	}

	public void setDefaultAddr(Boolean defaultAddr) {
		this.defaultAddr = defaultAddr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getGstinNumber() {
		return gstinNumber;
	}

	public void setGstinNumber(String gstinNumber) {
		this.gstinNumber = gstinNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AddressDTO)) return false;

		AddressDTO that = (AddressDTO) o;

		if (getBuildingNumber() != null ? !getBuildingNumber().equals(that.getBuildingNumber()) : that.getBuildingNumber() != null)
			return false;
		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getLine1() != null ? !getLine1().equals(that.getLine1()) : that.getLine1() != null) return false;
		if (getLine2() != null ? !getLine2().equals(that.getLine2()) : that.getLine2() != null) return false;
		if (getLine3() != null ? !getLine3().equals(that.getLine3()) : that.getLine3() != null) return false;
		if (getPincode() != null ? !getPincode().equals(that.getPincode()) : that.getPincode() != null) return false;
		if (getLandmark() != null ? !getLandmark().equals(that.getLandmark()) : that.getLandmark() != null)
			return false;
		if (getAddressType() != null ? !getAddressType().equals(that.getAddressType()) : that.getAddressType() != null)
			return false;
		if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
		if (getState() != null ? !getState().equals(that.getState()) : that.getState() != null) return false;
		if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
		if (getActive() != null ? !getActive().equals(that.getActive()) : that.getActive() != null) return false;
		if (getDefaultAddr() != null ? !getDefaultAddr().equals(that.getDefaultAddr()) : that.getDefaultAddr() != null)
			return false;
		if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
			return false;
		if (getDistrict() != null ? !getDistrict().equals(that.getDistrict()) : that.getDistrict() != null)
			return false;
		return getGstinNumber() != null ? getGstinNumber().equals(that.getGstinNumber()) : that.getGstinNumber() == null;
	}

	@Override
	public int hashCode() {
		int result = getBuildingNumber() != null ? getBuildingNumber().hashCode() : 0;
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		result = 31 * result + (getLine1() != null ? getLine1().hashCode() : 0);
		result = 31 * result + (getLine2() != null ? getLine2().hashCode() : 0);
		result = 31 * result + (getLine3() != null ? getLine3().hashCode() : 0);
		result = 31 * result + (getPincode() != null ? getPincode().hashCode() : 0);
		result = 31 * result + (getLandmark() != null ? getLandmark().hashCode() : 0);
		result = 31 * result + (getAddressType() != null ? getAddressType().hashCode() : 0);
		result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
		result = 31 * result + (getState() != null ? getState().hashCode() : 0);
		result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
		result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
		result = 31 * result + (getDefaultAddr() != null ? getDefaultAddr().hashCode() : 0);
		result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
		result = 31 * result + (getDistrict() != null ? getDistrict().hashCode() : 0);
		result = 31 * result + (getGstinNumber() != null ? getGstinNumber().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "AddressDTO{" +
				"buildingNumber='" + buildingNumber + '\'' +
				", id=" + id +
				", line1='" + line1 + '\'' +
				", line2='" + line2 + '\'' +
				", line3='" + line3 + '\'' +
				", pincode='" + pincode + '\'' +
				", landmark='" + landmark + '\'' +
				", addressType='" + addressType + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", active=" + active +
				", defaultAddr=" + defaultAddr +
				", description='" + description + '\'' +
				", district='" + district + '\'' +
				", gstinNumber='" + gstinNumber + '\'' +
				'}';
	}
}
