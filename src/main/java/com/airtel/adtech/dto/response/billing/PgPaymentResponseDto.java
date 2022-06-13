package com.airtel.adtech.dto.response.billing;

import java.math.BigDecimal;



import com.airtel.common.constants.enums.PaymentStatus;

public class PgPaymentResponseDto {
	
	private String transactionId;
	private PaymentStatus status;
	private BigDecimal amountReceived;
	private String mode;
	private String pg;
	private String remarks;
	
	
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public PaymentStatus getStatus() {
		return status;
	}
	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPg() {
		return pg;
	}
	public void setPg(String pg) {
		this.pg = pg;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
	

	
	
	
	

}
