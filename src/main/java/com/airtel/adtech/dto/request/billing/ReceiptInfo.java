package com.airtel.adtech.dto.request.billing;

import java.util.List;

import com.airtel.adtech.dto.mongo.ReceiptData;

public class ReceiptInfo {

	private List<ReceiptData> receipt;

	public List<ReceiptData> getReceipt() {
		return receipt;
	}

	public void setReceipt(List<ReceiptData> receipt) {
		this.receipt = receipt;
	}

	@Override
	public String toString() {
		return "ReceiptInfo [receipt=" + receipt + "]";
	}

	
		
		
		
		
	}


