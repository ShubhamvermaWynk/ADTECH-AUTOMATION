package com.airtel.adtech.dto.request.billing;

public class KafkaReceiptPushEventDTO {
	
	private String invoiceId;
	private ReceiptInfo receiptList;
	
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public ReceiptInfo getReceiptList() {
		return receiptList;
	}
	public void setReceiptList(ReceiptInfo receiptList) {
		this.receiptList = receiptList;
	}
	@Override
	public String toString() {
		return "KafkaReceiptPushEventDTO [invoiceId=" + invoiceId + ", receiptList=" + receiptList + "]";
	}
	
	
	

}
