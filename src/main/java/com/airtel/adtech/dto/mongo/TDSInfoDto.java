package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;

public class TDSInfoDto {
	
	private BigDecimal value;
	private String unit;

	public TDSInfoDto()
	{
		setUnit("PERCENTAGE");
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	

}
