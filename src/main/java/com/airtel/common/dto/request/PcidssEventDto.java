package com.airtel.common.dto.request;

import java.math.BigDecimal;

import com.airtel.common.constants.enums.PaymentEvent;
import com.airtel.common.constants.enums.PaymentStatus;

public class PcidssEventDto {
	private String pgId;
    private String orderId;
    private String pgStatus;
    private PaymentStatus paymentStatus;
    private BigDecimal paymentAmount;
    private PaymentEvent eventName; // can be PRE_PAYMENT,POST_PAYMENT
    private String errorCode;
    private String errorDescription;
    private Long paymentDate;// epoch

   
    private String cardRefNo;
    private String paymentMode;
    private String paymentGateway;
    private String pgSystemId;
    private String bankCode;
    private String cardNetwork;
    private String lob;
    
	public String getPgId() {
		return pgId;
	}
	public void setPgId(String pgId) {
		this.pgId = pgId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPgStatus() {
		return pgStatus;
	}
	public void setPgStatus(String pgStatus) {
		this.pgStatus = pgStatus;
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public PaymentEvent getEventName() {
		return eventName;
	}
	public void setEventName(PaymentEvent eventName) {
		this.eventName = eventName;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
	public Long getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getCardRefNo() {
		return cardRefNo;
	}
	public void setCardRefNo(String cardRefNo) {
		this.cardRefNo = cardRefNo;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public String getPgSystemId() {
		return pgSystemId;
	}
	public void setPgSystemId(String pgSystemId) {
		this.pgSystemId = pgSystemId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCardNetwork() {
		return cardNetwork;
	}
	public void setCardNetwork(String cardNetwork) {
		this.cardNetwork = cardNetwork;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	@Override
	public String toString() {
		return "PcidssEventDto [pgId=" + pgId + ", orderId=" + orderId + ", pgStatus=" + pgStatus
				+ ", paymentStatus=" + paymentStatus + ", paymentAmount=" + paymentAmount + ", eventName="
				+ eventName + ", errorCode=" + errorCode + ", errorDescription=" + errorDescription
				+ ", paymentDate=" + paymentDate + ", cardRefNo=" + cardRefNo + ", paymentMode=" + paymentMode
				+ ", paymentGateway=" + paymentGateway + ", pgSystemId=" + pgSystemId + ", bankCode=" + bankCode
				+ ", cardNetwork=" + cardNetwork + ", lob=" + lob + "]";
	}

}
