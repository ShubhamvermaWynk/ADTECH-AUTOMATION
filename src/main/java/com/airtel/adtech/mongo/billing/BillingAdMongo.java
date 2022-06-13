package com.airtel.adtech.mongo.billing;

import java.util.List;

import com.airtel.adtech.dto.mongo.InvoiceMeta;
import com.airtel.adtech.dto.mongo.TransactionDTO;

public interface BillingAdMongo {
	
	void updateCloseFlag(String transacId);
	InvoiceMeta getInvoiceMongoCollectionByCompanyId(String companyUuid);
	InvoiceMeta getInvoiceMongoCollectionByInvoiceId(String invoiceId);
	TransactionDTO getTransacMongoCollectionByid(String transacId);
	List<InvoiceMeta> getInvoiceMongoByTransId(String transacId, int size);
	
	void deleteInvoices(List<String> invoiceId);
	
}
