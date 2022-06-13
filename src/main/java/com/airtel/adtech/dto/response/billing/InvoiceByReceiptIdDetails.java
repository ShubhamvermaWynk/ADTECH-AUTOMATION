package com.airtel.adtech.dto.response.billing;

public class InvoiceByReceiptIdDetails {

    private String invoiceId;
    private String invoiceNumber;
    private String invoiceDate;
    private String dueDate;
    private String totalAmount;
    private String pendingAmount;
    private String status;
    private String documentUrl;


  



	public String getInvoiceId() { return invoiceId; }

    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getInvoiceNumber() { return invoiceNumber; }

    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getInvoiceDate() { return invoiceDate; }

    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }

    public String getDueDate() { return dueDate; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getTotalAmount() { return totalAmount; }

    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }

    public String getPendingAmount() { return pendingAmount; }

    public void setPendingAmount(String pendingAmount) { this.pendingAmount = pendingAmount; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
    public String getDocumentUrl() { return documentUrl; }

    public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; }

	@Override
	public String toString() {
		return "InvoiceByReceiptIdDetails [invoiceId=" + invoiceId + ", invoiceNumber=" + invoiceNumber
				+ ", invoiceDate=" + invoiceDate + ", dueDate=" + dueDate + ", totalAmount=" + totalAmount
				+ ", pendingAmount=" + pendingAmount + ", status=" + status + ", documentUrl=" + documentUrl + "]";
	}
    
    

   

}
