package com.airtel.adtech.dto.response.sms;

import java.util.List;
import java.util.Map;

import com.airtel.adtech.constants.enums.AdminOrderListKey;

public class AdminOrderListResponseDTO {
	
	AdminOrderListKey key;
	private long totalRecordsCount;
	List<Map<String,Object>> data;
	
	
	
	public AdminOrderListKey getKey() {
		return key;
	}
	public void setKey(AdminOrderListKey key) {
		this.key = key;
	}
	public long getTotalRecordsCount() {
		return totalRecordsCount;
	}
	public void setTotalRecordsCount(long totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AdminOrderListDTO [key=" + key + ", totalRecordsCount=" + totalRecordsCount + ", data=" + data + "]";
	}
	
	
	
	
	
	
	


}
