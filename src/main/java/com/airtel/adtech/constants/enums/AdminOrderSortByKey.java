package com.airtel.adtech.constants.enums;

public enum AdminOrderSortByKey {
	updatedAt("updatedAt"),
	createdAt("createdAt"),executionDate("campaignDuration.startDate"), submissionDate("submissionDate");

	private String value;

	private AdminOrderSortByKey(String value) {
		this.value = value;
	}
	

    public String value() {
        return value;
    }

}
