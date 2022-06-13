package com.airtel.adtech.manager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.testng.Assert;

import com.airtel.adtech.constants.AdtechApiConstants;
import com.airtel.adtech.constants.enums.InvoiceStatus;
import com.airtel.adtech.constants.enums.TransactionStatus;
import com.airtel.adtech.dto.mongo.InvoiceMeta;
import com.airtel.adtech.dto.mongo.TransactionDTO;
import com.airtel.adtech.dto.request.billing.KafkaReceiptPushEventDTO;
import com.airtel.adtech.dto.request.billing.ReceiptInfo;
import com.airtel.adtech.dto.mongo.ReceiptData;
import com.airtel.adtech.dto.mongo.PaymentInvoice;
import com.airtel.adtech.dto.mongo.TDSInfoDto;
import com.airtel.adtech.dto.response.billing.InitiatePaymentResponseDTO;
import com.airtel.adtech.dto.response.billing.InvoiceListDTO;

import com.airtel.adtech.entity.InvoiceSummary;
import com.airtel.adtech.mongo.billing.BillingAdMongo;
import com.airtel.common.constants.enums.PaymentEvent;
import com.airtel.common.constants.enums.PaymentStatus;
import com.airtel.common.dto.request.PcidssEventDto;
import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BillingAdtechApiManager extends CommonApi implements AdtechApiConstants {
	BillingAdMongo dao;
	CommonApi commonApi = new CommonApi();

	String mobileNumber;
	String bankCode, cardType, mode, gateway;
	BigDecimal gstPercentage, tdsPercentage;
	String kafkaIp, kafkaTopic;
	int port;

	String receiptId, receiptNumber;
	
	
	Properties teamsDataProperties;
	String expectedModeDb, expectedBankCode, expectedCard, expectedTransacId;
	String expectedErrorCode,expectedErrorDesc,expectedGateway,expectedPgDate,expectedPgStatus;

	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	private static final String AMOUNT = "amount";
	private static final String PERCENTAGE = "percentage";
	private static final String TOTAL_TDS = "totalTds";

	public BillingAdtechApiManager(Properties envProperty, Properties billingPropertyFile) {
		
		teamsDataProperties = commonApi.getTestDataConfigPropertyObject("teams");
		
		mobileNumber = envProperty.getProperty("MOBILE_NO");
		bankCode = billingPropertyFile.getProperty("BANK_CODE");
		cardType = billingPropertyFile.getProperty("CARD_TYPE");
		mode = billingPropertyFile.getProperty("MODE");
		gateway = billingPropertyFile.getProperty("GATEWAY");
		gstPercentage = new BigDecimal(billingPropertyFile.getProperty("GST_PERCENT"));
		tdsPercentage = new BigDecimal(billingPropertyFile.getProperty("TDS_PERCENT"));

		kafkaIp = envProperty.getProperty("KAFKA_IP");
		kafkaTopic = envProperty.getProperty("KAFKA_RECEIPT_TOPIC");
		port = Integer.parseInt(envProperty.getProperty("KAFKA_PORT"));

		receiptId = billingPropertyFile.getProperty("RECEIPT_ID");
		receiptNumber = billingPropertyFile.getProperty("RECEIPT_NUMBER");
		
		//teams data
		expectedBankCode =teamsDataProperties.getProperty("PAYMENT_BANK_CODE");
		expectedCard = teamsDataProperties.getProperty("PAYMENT_CARD");
		expectedModeDb = teamsDataProperties.getProperty("PAYMENT_MODE_DB");
		expectedGateway = teamsDataProperties.getProperty("PAYMENT_GATEWAY");
		
		expectedErrorCode = teamsDataProperties.getProperty("EVENT_ERROR");
		expectedErrorDesc = teamsDataProperties.getProperty("EVENT_DESC");
		expectedPgDate= teamsDataProperties.getProperty("PG_DATE");
		expectedPgStatus = teamsDataProperties.getProperty("PG_STATUS");

	}

	public TransactionDTO setTransacData(String orderId, TransactionStatus status, String companyUuid, String userUuid,
			String lobName, List<String> invoiceIds, List<String> invoiceNos, String pgTransacId, boolean closeFlag,
			List<BigDecimal> pendingAmount, Instant updatedAt, Instant createdAt, boolean recievedAmount) {
		TransactionDTO trasacData = new TransactionDTO();
		List<PaymentInvoice> invoicesList = new ArrayList<>();

		for (int i = 0; i < invoiceIds.size(); i++) {
			PaymentInvoice invoices = new PaymentInvoice();
			invoices.setInvoiceId(invoiceIds.get(i));
			invoices.setInvoiceNumber(invoiceNos.get(i));
			invoices.setAmount(pendingAmount.get(i));

			invoicesList.add(invoices);

		}

		BigDecimal totalAmount = invoicesList.stream().map(x -> x.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

		TransactionDTO.PaymentMetaInfo payMeta = trasacData.new PaymentMetaInfo();
		payMeta.setMobileNumber(mobileNumber);

		TransactionDTO.PaymentInstrument payInst = trasacData.new PaymentInstrument();
		// payInst.setBankCode(bankCode);
		// payInst.setCardType(cardType);
		// payInst.setGateway(gateway);
		payInst.setMode(mode);

		trasacData.setTransactionId(orderId);
		trasacData.setInvoices(invoicesList);

		trasacData.setStatus(status);
		trasacData.setAmount(getPayableAmount(totalAmount));

		if (recievedAmount) {
			trasacData.setReceivedAmount(getPayableAmount(totalAmount));
		}
		trasacData.setCompanyUuid(companyUuid);
		trasacData.setUserUuid(userUuid);
		trasacData.setInstrumentInfo(payInst);
		trasacData.setLobName(lobName);
		trasacData.setMetaInfo(payMeta);
		// trasacData.setPgTransactionId(pgTransacId);
		trasacData.setClosed(closeFlag);
		trasacData.setUpdatedAt(updatedAt);
		trasacData.setCreatedAt(createdAt);

		return trasacData;

	}

	public InvoiceMeta setInvoiceData(String companyUUid, String userUuid, String invoiceNo, String invoiceId,
			String transacId, InvoiceStatus status, BigDecimal totalAmount, List<BigDecimal> receiptAmount) {
		TDSInfoDto tds = new TDSInfoDto();
		tds.setValue(tdsPercentage);

		InvoiceMeta invoiceMeta = new InvoiceMeta();
		invoiceMeta.setAmount(totalAmount);
		invoiceMeta.setCompanyUuid(companyUUid);
		invoiceMeta.setInvoiceNumber(invoiceNo);

		if (receiptAmount != null) {
			invoiceMeta.setReceiptInfo(createReceiptData(receiptAmount));
		}

		invoiceMeta.setStatus(status);
		invoiceMeta.setInvoiceId(invoiceId);
		invoiceMeta.setTransactionId(transacId);
		invoiceMeta.setUserUuid(userUuid);
		invoiceMeta.setTdsInfo(tds);

		return invoiceMeta;

	}

	public boolean validateInvoice(Object expected, Object actual) {

		String expectedJson = convertToJsonwithObjectMapper(expected);
		String actualJson = convertToJsonwithObjectMapper(actual);

		boolean responseValidator = true;
		responseValidator = mappedResponseValidator(expectedJson, actualJson, INVOICE_DETAILS_MAPPER_FILEPATH);
		System.out.println("result" + responseValidator);
		if (!responseValidator) {
			ReportHelper.logValidationFailure("Difference in response in Invoice Meta", expectedJson, actualJson,
					"Mismatch in Json");
			Assert.assertTrue(responseValidator);
		}
		return responseValidator;
	}

	public ReceiptInfo createReceiptData(List<BigDecimal> amount) {
		ReceiptInfo receiptInfoobj = new ReceiptInfo();
		List<ReceiptData> receipt = new ArrayList<>();

		ReceiptData receiptObj = new ReceiptData();
		// ReceiptInfo.ReceiptData receiptObj= receiptInfoobj.new ReceiptData();

		receiptObj.setId(receiptId);
		receiptObj.setNumber(receiptNumber);

		for (BigDecimal receiptAmount : amount) {
			receiptObj.setAmount(receiptAmount);
		}

		receipt.add(receiptObj);

		receiptInfoobj.setReceipt(receipt);
		return receiptInfoobj;

	}

	public BigDecimal getTdsAmount(BigDecimal totalAmount) {
		BigDecimal taxableAmount = null;
		BigDecimal tdsAmount = null;

		taxableAmount = totalAmount.divide(
				BigDecimal.ONE.add(gstPercentage.divide(ONE_HUNDRED, 2, RoundingMode.HALF_EVEN)), 2,
				RoundingMode.HALF_EVEN);

		// taxableAmount=
		// totalAmount.multiply(gstPercentage.add(ONE_HUNDRED).divide(ONE_HUNDRED, 2,
		// RoundingMode.HALF_EVEN));
		tdsAmount = taxableAmount.multiply(tdsPercentage.divide(ONE_HUNDRED, 2, RoundingMode.HALF_EVEN));

		return tdsAmount.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getPayableAmount(BigDecimal totalAmount) {
		BigDecimal netTdsAmount = getTdsAmount(totalAmount);
		BigDecimal finalAmount = totalAmount.subtract(netTdsAmount);

		return finalAmount.setScale(2, RoundingMode.HALF_EVEN);

	}

	public InitiatePaymentResponseDTO validateTransaction(String orderId, String lobName, List<String> invoiceId,
			List<String> invoiceNos, List<BigDecimal> totalAmount) {
		InitiatePaymentResponseDTO paymentResponse = new InitiatePaymentResponseDTO();

		paymentResponse.setOrderId(orderId);
		paymentResponse.setBenefitAmount(totalAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		paymentResponse.setInvoiceCount(invoiceId.size());
		paymentResponse.setLobName(lobName);
		paymentResponse
				.setPaymentAmount(getPayableAmount(totalAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));

		List<InvoiceSummary> billSummary = new ArrayList<>();

		for (int i = 0; i < invoiceId.size(); i++) {
			InvoiceSummary invoiceData = new InvoiceSummary();
			invoiceData.setInvoiceId(invoiceId.get(i));
			invoiceData.setInvoiceNumber(invoiceNos.get(i));
			invoiceData.setPayableAmount(getPayableAmount(totalAmount.get(i)));
			invoiceData.setTdsAmount(getTdsAmount(totalAmount.get(i)));
			invoiceData.setTdsPercentage(tdsPercentage);
			invoiceData.setInvoiceAmount(totalAmount.get(i));

			billSummary.add(invoiceData);

		}

		Map<String, Object> totalDeductions = new HashMap<>();
		Map<String, Object> totalTds = new HashMap<>();

		totalTds.put(AMOUNT, getTdsAmount(totalAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));

		totalTds.put(PERCENTAGE, tdsPercentage);

		totalDeductions.put(TOTAL_TDS, totalTds);

		paymentResponse.setBillSummary(billSummary);
		paymentResponse.setTotalDeductions(totalDeductions);

		return paymentResponse;

	}

	public void pushReceiptEventinKafka(List<BigDecimal> receiptAmount, String invoiceId) {
		KafkaReceiptPushEventDTO receipts = new KafkaReceiptPushEventDTO();
		ReceiptInfo receipt = new ReceiptInfo();

		// ReceiptInfo.ReceiptData receiptdata= receipt.new ReceiptData();
		List<ReceiptData> listData = new ArrayList<>();
		ReceiptData receiptdata = new ReceiptData();

		for (BigDecimal amount : receiptAmount) {
			receiptdata.setAmount(amount);
		}

		receiptdata.setId(receiptId);
		receiptdata.setNumber(receiptNumber);

		listData.add(receiptdata);
		receipt.setReceipt(listData);

		receipts.setInvoiceId(invoiceId);
		receipts.setReceiptList(receipt);

		ObjectMapper obj = new ObjectMapper();

		try {
			CommonApi.produceKafkaEventasString(kafkaIp, port, kafkaTopic, null, obj.writeValueAsString(receipts));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Message sent successfully");
	}

	public List<InvoiceListDTO> expectedInvoiceList(List<Map<String, String>> list) {
		List<InvoiceListDTO> invoiceList = new ArrayList<>();

		for (Map<String, String> invoiceData : list) {
			InvoiceListDTO invoice = new InvoiceListDTO();
			invoice.setDocumentUrl(invoiceData.get("DOCUMENT_URL") != null ? true : false);

			String date = invoiceData.get("DUE_DATE");
			date = date.replace(" ", "T").concat("Z");
			invoice.setDueDate(date);

			invoice.setInvoiceId(invoiceData.get("TRANSACTION_ID"));
			invoice.setInvoiceNumber(invoiceData.get("TRANSACTION_NUMBER"));
			invoice.setPendingAmount(invoiceData.get("PENDING_AMOUNT"));
			invoice.setStatus(invoiceData.get("STATUS").contains("NOT_PAID") ? "OVERDUE" : invoiceData.get("STATUS"));
			invoice.setTotalAmount(invoiceData.get("TRANSACTION_AMOUNT"));
			invoiceList.add(invoice);

		}

		invoiceList = invoiceList.stream().sorted((t1, t2) -> t1.getInvoiceNumber().compareTo(t2.getInvoiceNumber()))
				.collect(Collectors.toList());

		return invoiceList;

	}
	
	
	public void produceEvent(PaymentStatus paymentstatus, String transcId, PaymentEvent eventName, String lob, String PgId,BigDecimal expectedPgAmount) {

		PcidssEventDto event = new PcidssEventDto();
		event.setBankCode(expectedBankCode);
		event.setCardNetwork(expectedCard);
		event.setErrorCode(expectedErrorCode);
		event.setErrorDescription(expectedErrorDesc);
		event.setEventName(eventName);
		event.setLob(lob);
		event.setOrderId(transcId);
		event.setPaymentAmount(expectedPgAmount);
		event.setPgId(PgId);
		
		Instant instant  = Instant.parse(expectedPgDate); 
	
		
		event.setPaymentGateway(expectedGateway);
		event.setPaymentMode(expectedModeDb);
		event.setPaymentStatus(paymentstatus);
		event.setPgStatus(expectedPgStatus);

		commonApi.produceKafkaEvent(kafkaIp, port, kafkaTopic, "3", event);
		System.out.println("Message sent successfully");
	}

}
