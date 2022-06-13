package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;

public class PaymentInvoice {

		private String invoiceId;
		private String invoiceNumber;
		private BigDecimal amount;
		
		
		
		
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
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		@Override
		public String toString() {
			return "PaymentInvoice [invoiceId=" + invoiceId + ", invoiceNumber=" + invoiceNumber + ", amount=" + amount
					+ "]";
		}
		
		
		
		
		
		
		
	
}
