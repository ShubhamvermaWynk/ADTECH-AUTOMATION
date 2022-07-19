package com.airtel.adtech.dto.response.billing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.airtel.adtech.entity.InvoiceSummary;

public class InitiatePaymentResponseDTO {
	
	public String orderId;
	public String lobName;
	public String circleId;
	public BigDecimal paymentAmount;
	public BigDecimal benefitAmount;
	public Integer invoiceCount;
	public List<InvoiceSummary> billSummary;
	public Map<String,Object> totalDeductions;
	
	
	
	
	public Map<String, Object> getTotalDeductions() {
		return totalDeductions;
	}
	public void setTotalDeductions(Map<String, Object> totalDeductions) {
		this.totalDeductions = totalDeductions;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getLobName() {
		return lobName;
	}
	public void setLobName(String lobName) {
		this.lobName = lobName;
	}
	public String getCircleId() {
		return circleId;
	}
	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
	
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public BigDecimal getBenefitAmount() {
		return benefitAmount;
	}
	public void setBenefitAmount(BigDecimal benefitAmount) {
		this.benefitAmount = benefitAmount;
	}
	public Integer getInvoiceCount() {
		return invoiceCount;
	}
	public void setInvoiceCount(Integer invoiceCount) {
		this.invoiceCount = invoiceCount;
	}
	
	
	
	
	public List<InvoiceSummary> getBillSummary() {
		return billSummary;
	}
	public void setBillSummary(List<InvoiceSummary> billSummary) {
		this.billSummary = billSummary;
	}
	@Override
	public String toString() {
		return "InitiatePaymentResponseDTO [orderId=" + orderId + ", lobName=" + lobName + ", circleId=" + circleId
				+ ", paymentAmount=" + paymentAmount + ", benefitAmount=" + benefitAmount + ", invoiceCount="
				+ invoiceCount + ", billSummary=" + billSummary + "]";
	}
	
	
	
	

	
	
}
