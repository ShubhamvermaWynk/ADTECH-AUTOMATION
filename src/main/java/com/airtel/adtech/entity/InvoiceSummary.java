package com.airtel.adtech.entity;

import java.math.BigDecimal;

public class InvoiceSummary {
	
	public String invoiceId;
	public String invoiceNumber;
	public BigDecimal tdsPercentage;
	public BigDecimal tdsAmount;
	public BigDecimal payableAmount;
	public BigDecimal invoiceAmount;
	
	
	
	
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public BigDecimal getTdsPercentage() {
		return tdsPercentage;
	}
	public void setTdsPercentage(BigDecimal tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}
	public BigDecimal getTdsAmount() {
		return tdsAmount;
	}
	public void setTdsAmount(BigDecimal tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	
	public BigDecimal getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
	@Override
	public String toString() {
		return "InvoiceSummary [invoiceId=" + invoiceId + ", invoiceNumber=" + invoiceNumber + ", tdsPercentage="
				+ tdsPercentage + ", tdsAmount=" + tdsAmount + ", payableAmount=" + payableAmount + "]";
	}
	
	
	

}
