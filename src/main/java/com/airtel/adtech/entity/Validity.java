package com.airtel.adtech.entity;

import com.airtel.adtech.constants.enums.ValidityUnit;

public class Validity {
	public ValidityUnit unit;
	public Integer value;
	
	
	
	public ValidityUnit getUnit() {
		return unit;
	}
	public void setUnit(ValidityUnit unit) {
		this.unit = unit;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Validity [unit=" + unit + ", value=" + value + "]";
	}
	

}
