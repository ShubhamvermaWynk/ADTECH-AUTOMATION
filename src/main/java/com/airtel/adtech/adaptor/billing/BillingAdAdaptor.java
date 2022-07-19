package com.airtel.adtech.adaptor.billing;
import java.math.BigDecimal;
import java.util.List;


import com.airtel.adtech.dto.response.billing.InitiatePaymentResponseDTO;
import com.airtel.adtech.dto.response.billing.InvoiceListDTO;
import com.airtel.adtech.dto.response.billing.PgPaymentResponseDto;

import com.airtel.adtech.dto.response.billing.ReceiptByInvoiceIdDetails;


import io.restassured.response.Response;


public interface BillingAdAdaptor {
	List<InvoiceListDTO> getInvoiceDetails(String companyUuid, String userId, String receiptId, String uniqueIdentifer);
	List<ReceiptByInvoiceIdDetails> getRecieptDetails(String companyUuid, String userId,  String invoiceId, String uniqueIdentifer);
	InitiatePaymentResponseDTO initiatePayment(List<String> InvoiceId, BigDecimal tds,String companyUuid, String userId, int expectedStatusCode,String uniqueIdentifer);
	List<PgPaymentResponseDto> fetchTransaction(String companyUuid, String userId, String uniqueIdentifer);
	Response validateTransaction(String transacId,String lobName, BigDecimal amount,String circleId,int expectedStatusCode , String uniqueIdentifer );
	Response getStatus(String transacId, int expectedStatusCode,String uniqueIdentifer);
	
	//teams validate payment api
	
	Response validatePayment(String transactionId, String lobName, BigDecimal amount, String circleId,
			int expectedStatusCode, String uniqueIdentifier);
	
	
}
