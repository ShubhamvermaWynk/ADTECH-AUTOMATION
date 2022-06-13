package com.airtel.adtech.dto.response.sms;

import java.math.BigDecimal;

public class FetchTargetandBudgetDTO {
	
	private BigDecimal budget;
	private long audienceReachCount;
	
	
	
	public BigDecimal getBudget() {
		return budget;
	}
	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}
	public long getAudienceReachCount() {
		return audienceReachCount;
	}
	public void setAudienceReachCount(long audienceReachCount) {
		this.audienceReachCount = audienceReachCount;
	}
	
	

}
