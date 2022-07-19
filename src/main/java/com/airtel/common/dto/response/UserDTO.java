package com.airtel.common.dto.response;

import com.airtel.common.constants.enums.Gender;

import java.util.Date;
import java.util.List;

public class UserDTO {
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String mobile;
	
	private String alternativeMobile;
	
	private String email;
	
	private Gender gender;
	
	private Date dob;
	
	private List<AddressDTO> addresses;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAlternativeMobile() {
		return alternativeMobile;
	}

	public void setAlternativeMobile(String alternativeMobile) {
		this.alternativeMobile = alternativeMobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserDTO)) return false;

		UserDTO userDTO = (UserDTO) o;

		if (getFirstName() != null ? !getFirstName().equals(userDTO.getFirstName()) : userDTO.getFirstName() != null)
			return false;
		if (getMiddleName() != null ? !getMiddleName().equals(userDTO.getMiddleName()) : userDTO.getMiddleName() != null)
			return false;
		if (getLastName() != null ? !getLastName().equals(userDTO.getLastName()) : userDTO.getLastName() != null)
			return false;
		if (getMobile() != null ? !getMobile().equals(userDTO.getMobile()) : userDTO.getMobile() != null) return false;
		if (getAlternativeMobile() != null ? !getAlternativeMobile().equals(userDTO.getAlternativeMobile()) : userDTO.getAlternativeMobile() != null)
			return false;
		if (getEmail() != null ? !getEmail().equals(userDTO.getEmail()) : userDTO.getEmail() != null) return false;
		if (getGender() != userDTO.getGender()) return false;
		if (getDob() != null ? !getDob().equals(userDTO.getDob()) : userDTO.getDob() != null) return false;
		return getAddresses() != null ? getAddresses().equals(userDTO.getAddresses()) : userDTO.getAddresses() == null;
	}

	@Override
	public int hashCode() {
		int result = getFirstName() != null ? getFirstName().hashCode() : 0;
		result = 31 * result + (getMiddleName() != null ? getMiddleName().hashCode() : 0);
		result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
		result = 31 * result + (getMobile() != null ? getMobile().hashCode() : 0);
		result = 31 * result + (getAlternativeMobile() != null ? getAlternativeMobile().hashCode() : 0);
		result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
		result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
		result = 31 * result + (getDob() != null ? getDob().hashCode() : 0);
		result = 31 * result + (getAddresses() != null ? getAddresses().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDTO [firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", mobile="
				+ mobile + ", alternativeMobile=" + alternativeMobile + ", email=" + email + ", gender=" + gender
				+ ", dob=" + dob + ", addresses=" + addresses + "]";
	}

}
