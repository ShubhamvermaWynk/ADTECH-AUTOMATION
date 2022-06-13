package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.airtel.adtech.constants.enums.TransactionStatus;

public class TransactionDTO {
	
	private String transactionId;

	private TransactionStatus status;

	private List<PaymentInvoice> invoices;

	private BigDecimal amount;

	private BigDecimal receivedAmount;

	private String pgTransactionId;

	private String lobName;

	private PaymentMetaInfo metaInfo;

	private PaymentInstrument instrumentInfo;

	private boolean closed;

	private String companyUuid;

	private String userUuid;

	private Instant createdAt;

	private Instant updatedAt;

	
	

	
	public class PaymentMetaInfo {
		private String mobileNumber;

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		
	}

	
	public class PaymentInstrument {
		private String bankCode;
		private String cardType;
		private String mode;
		private String gateway;
		
		public String getBankCode() {
			return bankCode;
		}
		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}
		public String getCardType() {
			return cardType;
		}
		public void setCardType(String cardType) {
			this.cardType = cardType;
		}
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
		public String getGateway() {
			return gateway;
		}
		public void setGateway(String gateway) {
			this.gateway = gateway;
		}
		
		
		
		

	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public TransactionStatus getStatus() {
		return status;
	}


	public void setStatus(TransactionStatus status) {
		this.status = status;
	}


	public List<PaymentInvoice> getInvoices() {
		return invoices;
	}


	public void setInvoices(List<PaymentInvoice> invoices) {
		this.invoices = invoices;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}


	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}


	public String getPgTransactionId() {
		return pgTransactionId;
	}


	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}


	public String getLobName() {
		return lobName;
	}


	public void setLobName(String lobName) {
		this.lobName = lobName;
	}


	public PaymentMetaInfo getMetaInfo() {
		return metaInfo;
	}


	public void setMetaInfo(PaymentMetaInfo metaInfo) {
		this.metaInfo = metaInfo;
	}


	public PaymentInstrument getInstrumentInfo() {
		return instrumentInfo;
	}


	public void setInstrumentInfo(PaymentInstrument instrumentInfo) {
		this.instrumentInfo = instrumentInfo;
	}


	public boolean isClosed() {
		return closed;
	}


	public void setClosed(boolean closed) {
		this.closed = closed;
	}


	public String getCompanyUuid() {
		return companyUuid;
	}


	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}


	public String getUserUuid() {
		return userUuid;
	}


	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}


	public Instant getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}


	public Instant getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}


	@Override
	public String toString() {
		return "TransactionDTO [transactionId=" + transactionId + ", status=" + status + ", invoices=" + invoices
				+ ", amount=" + amount + ", receivedAmount=" + receivedAmount + ", pgTransactionId=" + pgTransactionId
				+ ", lobName=" + lobName + ", metaInfo=" + metaInfo + ", instrumentInfo=" + instrumentInfo + ", closed="
				+ closed + ", companyUuid=" + companyUuid + ", userUuid=" + userUuid + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	
	
	

}
