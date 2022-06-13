package com.airtel.adtech.dto.request.sms;

import java.time.Instant;
import java.util.List;

import com.airtel.adtech.constants.enums.AdminOrderListKey;
import com.airtel.adtech.constants.enums.AdminOrderSortByKey;
import com.airtel.adtech.constants.enums.SortDirection;

public class AdminOrderListDTO {
	
	private GroupBy groupBy;
	private SortBy sortBy;
	private Range range;
	private List<Filters> filters;


	public static class GroupBy {
		private AdminOrderListKey groupId;
		private String key;
		private String value;
		
		public GroupBy()
		{
			this.setKey("status");
		}
		
		
		public AdminOrderListKey getGroupId() {
			return groupId;
		}
		public void setGroupId(AdminOrderListKey groupId) {
			this.groupId = groupId;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		

	}


	public static class SortBy {

		private AdminOrderSortByKey key;
		private SortDirection direction;
		
		
		public AdminOrderSortByKey getKey() {
			return key;
		}
		public void setKey(AdminOrderSortByKey key) {
			this.key = key;
		}
		public SortDirection getDirection() {
			return direction;
		}
		public void setDirection(SortDirection direction) {
			this.direction = direction;
		}
	

	}


	public static class Range {
		private String key;
		private Instant startDate;
		private Instant endDate;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Instant getStartDate() {
			return startDate;
		}
		public void setStartDate(Instant startDate) {
			this.startDate = startDate;
		}
		public Instant getEndDate() {
			return endDate;
		}
		public void setEndDate(Instant endDate) {
			this.endDate = endDate;
		}
		
		
		

	}


	public static class Filters {
		private String type;
		private Payload payload;
		
		public Filters()
		{
			this.setType("SEARCH");
		}
	
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Payload getPayload() {
			return payload;
		}
		public void setPayload(Payload payload) {
			this.payload = payload;
		}
		
		
		
	}


	public static class Payload {
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
	}


	public GroupBy getGroupBy() {
		return groupBy;
	}


	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}


	public SortBy getSortBy() {
		return sortBy;
	}


	public void setSortBy(SortBy sortBy) {
		this.sortBy = sortBy;
	}


	public Range getRange() {
		return range;
	}


	public void setRange(Range range) {
		this.range = range;
	}


	public List<Filters> getFilters() {
		return filters;
	}


	public void setFilters(List<Filters> filters) {
		this.filters = filters;
	}


	@Override
	public String toString() {
		return "AdminOrderListDTO [groupBy=" + groupBy + ", sortBy=" + sortBy + ", range=" + range + ", filters="
				+ filters + ", getGroupBy()=" + getGroupBy() + ", getSortBy()=" + getSortBy() + ", getRange()="
				+ getRange() + ", getFilters()=" + getFilters() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	

}
