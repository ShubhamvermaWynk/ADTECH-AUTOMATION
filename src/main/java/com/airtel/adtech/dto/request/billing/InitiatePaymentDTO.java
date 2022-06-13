package com.airtel.adtech.dto.request.billing;

import java.math.BigDecimal;
import java.util.List;

public class InitiatePaymentDTO {
	
	List<String> invoices;
	BigDecimal  tdsPercentage;
	
	
	public List<String> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<String> invoices) {
		this.invoices = invoices;
	}
	public BigDecimal getTdsPercentage() {
		return tdsPercentage;
	}
	public void setTdsPercentage(BigDecimal tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}
	
	

}
