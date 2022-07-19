package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;
import java.util.List;

import com.airtel.adtech.constants.enums.InvoiceStatus;
import com.airtel.adtech.dto.request.billing.ReceiptInfo;

public class InvoiceMeta {
	
	private String id;
	private String companyUuid;
	private String userUuid;
	private String invoiceId;
	private String invoiceNumber;
	private BigDecimal amount;
	private InvoiceStatus status;
	private String transactionId;
	private ReceiptInfo receiptInfo;
	private boolean archived;
	private TDSInfoDto tdsInfo;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		invoiceId = invoiceId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public InvoiceStatus getStatus() {
		return status;
	}
	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public ReceiptInfo getReceiptInfo() {
		return receiptInfo;
	}
	public void setReceiptInfo(ReceiptInfo receiptInfo) {
		this.receiptInfo = receiptInfo;
	}
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	public TDSInfoDto getTdsInfo() {
		return tdsInfo;
	}
	public void setTdsInfo(TDSInfoDto tdsInfo) {
		this.tdsInfo = tdsInfo;
	}
	@Override
	public String toString() {
		return "InvoiceMeta [id=" + id + ", companyUuid=" + companyUuid + ", userUuid=" + userUuid + ", InvoiceId="
				+ invoiceId + ", invoiceNumber=" + invoiceNumber + ", amount=" + amount + ", status=" + status
				+ ", transactionId=" + transactionId + ", receiptInfo=" + receiptInfo + ", archived=" + archived
				+ ", tdsInfo=" + tdsInfo + "]";
	}
	
	
	
	

	




	
	
	

}
