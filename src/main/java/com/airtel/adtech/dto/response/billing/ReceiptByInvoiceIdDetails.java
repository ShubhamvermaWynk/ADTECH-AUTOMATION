package com.airtel.adtech.dto.response.billing;

public class ReceiptByInvoiceIdDetails {

    private String receiptId;
    private String receiptNumber;
    private String receiptAmount;
    private String receiptDate;
    private String status;

    public String getReceiptId() { return receiptId; }

    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }

    public String getReceiptNumber() { return receiptNumber; }

    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getReceiptAmount() { return receiptAmount; }

    public void setReceiptAmount(String receiptAmount) { this.receiptAmount = receiptAmount; }

    public String getReceiptDate() { return receiptDate; }

    public void setReceiptDate(String receiptDate) { this.receiptDate = receiptDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ReceiptByInvoiceIdDetails{" +
                "receiptId='" + receiptId + '\'' +
                ", receiptNumber='" + receiptNumber + '\'' +
                ", receiptAmount='" + receiptAmount + '\'' +
                ", receiptDate='" + receiptDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
