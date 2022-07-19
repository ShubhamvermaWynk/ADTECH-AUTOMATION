package com.airtel.common.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class UserCompanyResponseVo {
	private UserDTO user;
	private CompanyDTO company;
	private Map<String, Object> services;
	
	
	public Map<String, Object> getServices() {
		return services;
	}

	public void setServices(Map<String, Object> services) {
		this.services = services;
	}

	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public CompanyDTO getCompany() {
		return company;
	}
	
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserCompanyResponseVo)) return false;

		UserCompanyResponseVo that = (UserCompanyResponseVo) o;

		if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) return false;
		return getCompany() != null ? getCompany().equals(that.getCompany()) : that.getCompany() == null;
	}

	@Override
	public int hashCode() {
		int result = getUser() != null ? getUser().hashCode() : 0;
		result = 31 * result + (getCompany() != null ? getCompany().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserCompanyResponseVo [user=" + user + ", company=" + company + ", services=" + services + "]";
	}
}
