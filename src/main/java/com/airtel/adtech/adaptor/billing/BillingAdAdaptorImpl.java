package com.airtel.adtech.adaptor.billing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.dto.request.billing.InitiatePaymentDTO;
import com.airtel.adtech.dto.request.billing.ValidatePaymentDTO;
import com.airtel.adtech.dto.response.billing.InitiatePaymentResponseDTO;
import com.airtel.adtech.dto.response.billing.InvoiceListDTO;
import com.airtel.adtech.dto.response.billing.PgPaymentResponseDto;

import io.restassured.http.Header;
import io.restassured.response.Response;

import com.airtel.adtech.dto.response.billing.ReceiptByInvoiceIdDetails;

import com.airtel.teams.common.CommonApi;
import com.google.gson.reflect.TypeToken;

public class BillingAdAdaptorImpl extends CommonApi implements BillingAdAdaptor, AdtechApiConstants {

	String serverInitials;
	String teamPaymentInitials;
	static final String receiptId = "receiptId";
	static final String invoiceId = "invoiceId";

	public BillingAdAdaptorImpl(Properties envProperty) {
		serverInitials = envProperty.getProperty("ADTECH_BILLING");
		teamPaymentInitials= envProperty.getProperty("TEAM_BILLING");
	}

	CommonApi commonApi = new CommonApi();

	public List<ReceiptByInvoiceIdDetails> getRecieptDetails(String companyUuid, String userId, String invoiceId,
			String uniqueIdentifer) {

		Map<String, String> queryParams = new HashMap<>();
		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Type listType = new TypeToken<List<ReceiptByInvoiceIdDetails>>() {
		}.getType();
		if (invoiceId != null) {
			queryParams.put(invoiceId, invoiceId);
		}
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				FETCH_RECEIPT_BILLING + companyUuid + "/list", true, false, null, extraHeaders, uniqueIdentifer);
		return commonApi.convertFromJsonArray(response.asString(), listType);
	}

	public List<InvoiceListDTO> getInvoiceDetails(String companyUuid, String userId, String receiptId,
			String uniqueIdentifer) {

		Map<String, String> queryParams = new HashMap<>();
		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		if (receiptId != null) {
			queryParams.put(receiptId, receiptId);
		}

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				FETCH_INVOICE_BILLING + companyUuid + "/list", true, false, null, extraHeaders, uniqueIdentifer);

		Type listType = new TypeToken<List<InvoiceListDTO>>() {
		}.getType();
		return commonApi.convertFromJsonArray(response.asString(), listType);
	}

	@Override
	public InitiatePaymentResponseDTO initiatePayment(List<String> InvoiceId, BigDecimal tds, String companyUuid,
			String userId, int expectedStatusCode, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		if(userId!=null) {
		extraHeaders.add(new Header("USER-ID", userId));}

		InitiatePaymentDTO initiatePay = new InitiatePaymentDTO();
		initiatePay.setInvoices(InvoiceId);
		if (tds != null) {
			initiatePay.setTdsPercentage(tds);
		}

		String body = convertToJsonwithObjectMapper(initiatePay);

		Response response = getPostResponseWithStatusCode(body, serverInitials,
				INITIATE_PAYMENT_BILLING + companyUuid + "/initiate", true, expectedStatusCode, null, extraHeaders,
				uniqueIdentifer);

		InitiatePaymentResponseDTO initiateResponse = commonApi
				.convertFromJsonwithObjectMapperwithclass(response.asString(), InitiatePaymentResponseDTO.class);

		return initiateResponse;

	}

	@Override
	public List<PgPaymentResponseDto> fetchTransaction(String companyUuid, String userId, String uniqueIdentifer) {

		List<Header> extraHeaders = new ArrayList<>();
		extraHeaders.add(new Header("USER-ID", userId));

		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
				INITIATE_TRANSACTION_BILLING + companyUuid + "/list", true, false, null, extraHeaders, uniqueIdentifer);

		
		
		Type listType = new TypeToken<List<PgPaymentResponseDto>>() {
		}.getType();

		return commonApi.convertFromJsonArray(response.asString(), listType);

	}

	@Override
	public Response validateTransaction(String transacId, String lobName, BigDecimal amount, String circleId,
			int expectedStatusCode, String uniqueIdentifer) {

		ValidatePaymentDTO validateObj = new ValidatePaymentDTO();
		validateObj.setAmount(amount);
		validateObj.setCircleId(circleId);
		validateObj.setLobName(lobName);
		validateObj.setTransactionId(transacId);

		String body = convertToJson(validateObj);

		Response response = getPostResponseWithStatusCode(body, serverInitials, VALIDATE_BILLING, true,
				expectedStatusCode, null, null, uniqueIdentifer);

		return response;
	}

	@Override
	public Response getStatus(String transacId,int expectedStatusCode, String uniqueIdentifer) {
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("transactionId", transacId);
		
		Response response = getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, queryParams,
				TRANSACTION_STATUS , false, false, null, null, uniqueIdentifer);
		return response;
	}
	
	public Response validatePayment(String transactionId, String lobName, BigDecimal amount, String circleId,
			int expectedStatusCode, String uniqueIdentifier) {
		
		ValidatePaymentDTO validateRequestDTO= new ValidatePaymentDTO();
		validateRequestDTO.setAmount(amount);
		if(circleId!=null) {
		validateRequestDTO.setCircleId(circleId);}
		validateRequestDTO.setLobName(lobName);
		validateRequestDTO.setTransactionId(transactionId);
		
		String body=convertToJson(validateRequestDTO);

        return getPostResponseWithStatusCode(body, teamPaymentInitials, VALIDATE_PAYMENT_PATH, true, expectedStatusCode, null, null, uniqueIdentifier);
		
	}

}
