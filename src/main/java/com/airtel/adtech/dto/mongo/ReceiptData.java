package com.airtel.adtech.dto.mongo;

import java.math.BigDecimal;

public class ReceiptData {
	
		
		private String id;
		private String number;
		private BigDecimal amount;
	
		

		

		

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		@Override
		public String toString() {
			return "ReceiptData [id=" + id + ", number=" + number + ", amount=" + amount + "]";
		}
	
		

}
