package com.airtel.RestAssured.Adtech.Tests.billing;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.airtel.adtech.adaptor.billing.BillingAdAdaptorImpl;
import com.airtel.adtech.constants.enums.InvoiceStatus;
import com.airtel.adtech.constants.enums.TransactionStatus;
import com.airtel.adtech.dto.mongo.InvoiceMeta;
import com.airtel.adtech.dto.mongo.TransactionDTO;
import com.airtel.adtech.dto.response.billing.InitiatePaymentResponseDTO;
import com.airtel.adtech.dto.response.billing.PgPaymentResponseDto;
import com.airtel.adtech.manager.BillingAdtechApiManager;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.mongo.billing.BillingAdMongo;
import com.airtel.adtech.mongo.billing.BillingAdMongoImpl;
import com.airtel.dao.billing.BillingDao;
import com.airtel.dao.billing.BillingDaoImpl;
import com.airtel.adtech.dto.response.billing.InvoiceByReceiptIdDetails;
import com.airtel.adtech.dto.response.billing.ReceiptByInvoiceIdDetails;
import com.airtel.helper.report.ReportHelper;


import com.airtel.teams.common.CommonApi;
import com.airtel.common.constants.*;
import com.airtel.common.constants.enums.PaymentEvent;
import com.airtel.common.constants.enums.PaymentStatus;
import com.airtel.common.variables.*;


import com.airtel.adtech.adaptor.billing.BillingAdAdaptor;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ReceiptTest extends ReportHelper {

	CommonApi commonApi = new CommonApi();
	BillingDao billingDao;
	
	BillingAdAdaptor adaptor;
	String uniqueIdentifier;
	Connection billingCon;
	String customerArId;
	Properties billingDataPropertyFile, adtechbillingDataPropertyFile;
	
	String invoiceId, invoiceNo, invoicePartialPaid = null;
	String pgId;
	BillingAdMongo billingmongo;
	BillingAdtechApiManager adtechbillingApiManager;
	List<String> invoices;
	BigDecimal tds;

	Properties orderDataPropertyFile, onboardingDataPropertyFile, paymentDataPropertyFile;

	List<String> pendingAmount;
	List<String> invoiceNos;
	List<String> invoiceIds;
	List<BigDecimal> bigDecimalList;

	String circleId;
	OnboardApiManager onboardApiManager;
	OrderGlobalVariables orderGlobalVariables;

	@Parameters({ "environment" })
	@BeforeClass
	public void setEnvironment(@Optional("qa_1") String environment) {
		
		Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
		onboardApiManager=new OnboardApiManager(envPropertyFile);
		billingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("billing");
		adtechbillingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("adtechbilling");

		orderDataPropertyFile = commonApi.getTestDataConfigPropertyObject("order");

		onboardingDataPropertyFile = commonApi.getTestDataConfigPropertyObject("onboarding");

		paymentDataPropertyFile = commonApi.getTestDataConfigPropertyObject("payment");

		InitOrderVariables initOrderVariables = new InitOrderVariables();
		orderGlobalVariables = initOrderVariables.initializeOrderVariables(envPropertyFile);

		adaptor = new BillingAdAdaptorImpl(envPropertyFile);
		billingDao = new BillingDaoImpl();
		billingmongo = new BillingAdMongoImpl(envPropertyFile);
		adtechbillingApiManager = new BillingAdtechApiManager(envPropertyFile, billingDataPropertyFile);

	

		

		customerArId = billingDataPropertyFile.getProperty("AR_CUSTOMER_ID");
		invoiceNo = adtechbillingDataPropertyFile.getProperty("INVOICENO_NOTPAID");
		invoiceId = adtechbillingDataPropertyFile.getProperty("INVOICEID_NOTPAID");
		invoicePartialPaid = adtechbillingDataPropertyFile.getProperty("INVOICEID_PARTIALPAID");
		tds = new BigDecimal(adtechbillingDataPropertyFile.getProperty("TDS"));
		circleId = adtechbillingDataPropertyFile.getProperty("CIRCLE_ID");

		uniqueIdentifier = CommonApi.uniqueidentifier();

		
		pgId = RandomStringUtils.randomNumeric(10);
		
		List<Map<String, String>> transacDao = billingDao
				.getTransactionsByArCustomerIdandStatus(orderGlobalVariables.getBillingCon(), customerArId, "NOT_PAID");

		pendingAmount = transacDao.stream().map(x -> x.get("PENDING_AMOUNT")).collect(Collectors.toList());
		invoiceNos = transacDao.stream().map(x -> x.get("TRANSACTION_NUMBER")).collect(Collectors.toList());
		invoiceIds = transacDao.stream().map(x -> x.get("TRANSACTION_ID")).collect(Collectors.toList());

		bigDecimalList = pendingAmount.stream().map(BigDecimal::new).collect(Collectors.toList());

	}

	@BeforeMethod
	public void createUserCompany() {
		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		billingDao.updateCompanybyArId(orderGlobalVariables.getBillingCon(), customerArId,
				orderGlobalVariables.getCompanyUuid());
	}

	@Test(priority = 1)
	public void validateInitiatePaymentSuccess() throws InterruptedException {

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		InitiatePaymentResponseDTO expectedResponse = adtechbillingApiManager.validateTransaction(paymentResponse.getOrderId(), paymentResponse.getLobName(), invoiceIds, invoiceNos, bigDecimalList);
		commonApi.compareObjects(expectedResponse, paymentResponse, null);

		billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId()).getStatus().equals(TransactionStatus.INITIALISED);
		billingmongo.getInvoiceMongoByTransId(paymentResponse.getOrderId(), invoiceIds.size()).get(0).getStatus().equals(InvoiceStatus.INITIALIZED);

		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getPaymentAmount();

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),	PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(4000);
		TransactionDTO actualtransaction = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		List<InvoiceMeta> actualinvoice = billingmongo.getInvoiceMongoByTransId(paymentResponse.getOrderId(), invoiceIds.size());

		TransactionDTO expectedTransac = adtechbillingApiManager.setTransacData(paymentResponse.getOrderId(),TransactionStatus.SUCCESS, orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),paymentResponse.getLobName(), invoiceIds, invoiceNos, pgId, false, bigDecimalList,actualtransaction.getUpdatedAt(), actualtransaction.getCreatedAt(), true);
		InvoiceMeta expectedInvoice = adtechbillingApiManager.setInvoiceData(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), invoiceNos.get(0), invoiceIds.get(0), paymentResponse.getOrderId(),InvoiceStatus.PAYMENT_SUCCESS, new BigDecimal(pendingAmount.get(0)), null);

		commonApi.compareObjectswithObjectMapper(expectedTransac, actualtransaction, null);
		commonApi.compareObjectswithObjectMapper(expectedInvoice, actualinvoice.get(0), null);

	}

	@Test(priority = 2)
	public void validateInitiatePaymentFailure() throws InterruptedException {

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),	paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getPaymentAmount();

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_FAILED, paymentResponse.getOrderId(),PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(3000);
		TransactionDTO transaction = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		List<InvoiceMeta> actualinvoice = billingmongo.getInvoiceMongoByTransId(paymentResponse.getOrderId(), invoiceIds.size());

		TransactionDTO expectedTransac = adtechbillingApiManager.setTransacData(paymentResponse.getOrderId(),TransactionStatus.FAILED, orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(),paymentResponse.getLobName(), invoiceIds, invoiceNos, pgId, true, bigDecimalList,transaction.getUpdatedAt(), transaction.getCreatedAt(), true);
		
		InvoiceMeta expectedInvoice = adtechbillingApiManager.setInvoiceData(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), invoiceNos.get(0), invoiceIds.get(0), paymentResponse.getOrderId(),
				InvoiceStatus.PAYMENT_FAILED, new BigDecimal(pendingAmount.get(0)), null);

		commonApi.compareObjectswithObjectMapper(expectedTransac, transaction, null);
		commonApi.compareObjectswithObjectMapper(expectedInvoice, actualinvoice.get(0), null);

	}

	@Test(priority = 3)
	public void initiatePaymentwithInvalidAmount() throws InterruptedException {

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				BigDecimal.valueOf(9092.0), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(3000);
		TransactionDTO actualTransaction = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());

		List<InvoiceMeta> actualinvoice = billingmongo.getInvoiceMongoByTransId(paymentResponse.getOrderId(), 1);

		if (!(actualTransaction.getStatus() == (TransactionStatus.INITIALISED))
				|| !(actualinvoice.get(0).getStatus().equals(InvoiceStatus.INITIALIZED))) {
			ReportHelper.logValidationFailure("Invalid Transaction Status", TransactionStatus.INITIALISED.name(),
					actualTransaction.getStatus().toString(), "Invalid Status");
			Assert.assertTrue(false);

		}
	}

	@Test(priority = 4)
	public void initiatePaymentOnPartialPaidInvoice() {
		adaptor.initiatePayment(Arrays.asList(invoicePartialPaid), tds, orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);
	}

	@Test(priority = 5)
	public void initiatePaymentWithInvalidEvent() throws InterruptedException {
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING_TEST, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(3000);
		TransactionDTO actualTransaction = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		List<InvoiceMeta> actualinvoice = billingmongo.getInvoiceMongoByTransId(paymentResponse.getOrderId(), 1);

		if (!(actualTransaction.getStatus().equals(TransactionStatus.VALIDATED))
				|| !(actualinvoice.get(0).getStatus().equals(InvoiceStatus.INITIALIZED))) {
			ReportHelper.logValidationFailure("Invalid Transaction Status", TransactionStatus.VALIDATED.name(),
					actualTransaction.getStatus().toString(), "Invalid Status");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 6)
	public void initiatePaymentWithMultipleInvoices() throws InterruptedException

	{
		billingDao.updateTransactionByTransactiontype(orderGlobalVariables.getBillingCon(), "INV", customerArId);
		List<Map<String, String>> transacDao = billingDao
				.getTransactionsByArCustomerIdandStatus(orderGlobalVariables.getBillingCon(), customerArId, "NOT_PAID");

		List<String> invoiceIds = transacDao.stream().map(x -> x.get("TRANSACTION_ID")).collect(Collectors.toList());

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());

		Thread.sleep(4000);
		TransactionDTO transaction = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		if (transaction.getInvoices().size() != invoiceIds.size()) {
			ReportHelper.logValidationFailure("Invalid Invoice Size", String.valueOf(invoiceIds.size()),
					String.valueOf(transaction.getInvoices().size()), "Invalid Size");
			Assert.assertTrue(false);

		}

		billingmongo.getInvoiceMongoByTransId(transaction.getTransactionId(), invoiceIds.size());

	}

	

	@Test(priority = 8)
	public void verifyValidateAPITwice() {

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);
		Response response = adaptor.validatePayment(paymentResponse.getOrderId(),
				paymentResponse.getLobName(), paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);
		JSONObject json = new JSONObject(response.asString());
		String obj = json.getString("errorCode");
		if (!obj.equals("INVALID_TRANSACTION")) {
			ReportHelper.logValidationFailure("Invalid Status", "INVALID_TRANSACTION", obj, "Invalid status");
			Assert.assertTrue(false);
		}

	}

	@Test(priority = 9)
	public void initiatePaymentOnProceesedStatus() {
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);
		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adaptor.initiatePayment(Arrays.asList(invoiceId), tds, orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);

	}

	@Test(priority = 10)
	public void validateInvoiceAndReceipt() throws InterruptedException {

		billingmongo.deleteInvoices(Arrays.asList(invoiceId));
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(4000);
		adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(expectedPgAmount), invoiceId);

		Thread.sleep(4000);
		InvoiceMeta invoiceMongoData = billingmongo
				.getInvoiceMongoCollectionByCompanyId(orderGlobalVariables.getCompanyUuid());
		InvoiceMeta expectedInvoiceReceipt = adtechbillingApiManager.setInvoiceData(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), invoiceNo, invoiceId, paymentResponse.getOrderId(),
				InvoiceStatus.POSTING_COMPLETED, new BigDecimal(pendingAmount.get(0)), Arrays.asList(expectedPgAmount));
		
		adtechbillingApiManager.validateInvoice(invoiceMongoData,expectedInvoiceReceipt);
		
		
		TransactionDTO transacMongoData = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		if (!transacMongoData.isClosed()) {
			ReportHelper.logValidationFailure("Invalid Transaction Closed flag", "true",
					String.valueOf(transacMongoData.isClosed()), "Invalid Status");
			Assert.assertTrue(false);

		}

	}
	


	@Test(priority = 11)
	public void validateInvoiceReceiptWithLessInvoiceAmount() throws InterruptedException {

		billingmongo.deleteInvoices(Arrays.asList(invoiceId));
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());

		Thread.sleep(4000);
		adtechbillingApiManager.pushReceiptEventinKafka(
				Arrays.asList(paymentResponse.getPaymentAmount().subtract(BigDecimal.ONE)), invoiceId);

		Thread.sleep(2000);
		InvoiceMeta invoiceMongoData = billingmongo.getInvoiceMongoCollectionByCompanyId(orderGlobalVariables.getCompanyUuid());

		if (invoiceMongoData.getStatus().toString() != "PAYMENT_SUCCESS") {
			ReportHelper.logValidationFailure("Invalid Invoice Status", "PAYMENT_SUCCESS",
					String.valueOf(invoiceMongoData.getStatus()), "Invalid Status");
			Assert.assertTrue(false);

		}
		
		if(invoiceMongoData.getReceiptInfo()!=null)
		{	
			Assert.assertTrue(false);
			
		}
		
		
		TransactionDTO transacMongoData = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		if (transacMongoData.isClosed()) {
			ReportHelper.logValidationFailure("Invalid Transaction Closed flag", "false",String.valueOf(transacMongoData.isClosed()), "Invalid Status");
			Assert.assertTrue(false);

		}
	}

	@Test(priority = 12)
	public void verifyReceiptsforMultipleInvoices() throws InterruptedException {

		billingDao.updateTransactionByTransactiontype(orderGlobalVariables.getBillingCon(), "INV", customerArId);
		List<Map<String, String>> transacDao = billingDao
				.getTransactionsByArCustomerIdandStatus(orderGlobalVariables.getBillingCon(), customerArId, "NOT_PAID");

		List<String> invoiceIds = transacDao.stream().map(x -> x.get("TRANSACTION_ID")).collect(Collectors.toList());
		
		billingmongo.deleteInvoices(invoiceIds);
		

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());

		Thread.sleep(2000);
		for (int i = 0; i < paymentResponse.getBillSummary().size(); i++) {
			adtechbillingApiManager.pushReceiptEventinKafka(
					Arrays.asList(paymentResponse.getPaymentAmount()), invoiceIds.get(i));
		}

		Thread.sleep(4000);
		for (String invoiceId : invoiceIds) {
			InvoiceMeta invoiceMongoData = billingmongo.getInvoiceMongoCollectionByInvoiceId(invoiceId);
			if (invoiceMongoData.getStatus().toString() != "POSTING_COMPLETED") {
				ReportHelper.logValidationFailure("Invalid Invoice Status", "POSTING_COMPLETED",
						String.valueOf(invoiceMongoData.getStatus()), "Invalid Status");
				Assert.assertTrue(false);
			}

		}
		TransactionDTO transacMongoData = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());
		if (!transacMongoData.isClosed()) {
			ReportHelper.logValidationFailure("Invalid Transaction Closed flag", "true",String.valueOf(transacMongoData.isClosed()), "Invalid Status");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 13)
	public void verifyReceiptsforMultipleInvoiceswithPendingReceipts() throws InterruptedException {

		billingDao.updateTransactionByTransactiontype(orderGlobalVariables.getBillingCon(), "INV", customerArId);
		List<Map<String, String>> transacDao = billingDao
				.getTransactionsByArCustomerIdandStatus(orderGlobalVariables.getBillingCon(), customerArId, "NOT_PAID");

		List<String> invoiceIds = transacDao.stream().map(x -> x.get("TRANSACTION_ID")).collect(Collectors.toList());
		
		billingmongo.deleteInvoices(invoiceIds);

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());

		Thread.sleep(2000);

		for (int i = 0; i < paymentResponse.getBillSummary().size() - 1; i++) {
			adtechbillingApiManager.pushReceiptEventinKafka(
					Arrays.asList(paymentResponse.getPaymentAmount()), invoiceIds.get(i));
		}

		Thread.sleep(2000);
		for (int i = 0; i < invoiceIds.size() - 1; i++) {
			InvoiceMeta invoiceMongoData = billingmongo.getInvoiceMongoCollectionByInvoiceId(invoiceIds.get(i));
			if (invoiceMongoData.getStatus().toString() != "POSTING_COMPLETED") {
				ReportHelper.logValidationFailure("Invalid Invoice Status", "POSTING_COMPLETED",
						String.valueOf(invoiceMongoData.getStatus()), "Invalid Status");
				Assert.assertTrue(false);
			}

		}
		TransactionDTO transacMongoData = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());

		if (transacMongoData.isClosed()) {
			ReportHelper.logValidationFailure("Invalid Transaction Closed flag", "false",
					String.valueOf(transacMongoData.isClosed()), "Invalid Status");
			Assert.assertTrue(false);

		}

	}

	@Test(priority = 14)
	public void verifyReceiptswithGreaterAmountThanInvoice() throws InterruptedException {

		billingDao.updateTransactionByTransactiontype(orderGlobalVariables.getBillingCon(), "INV", customerArId);
		List<Map<String, String>> transacDao = billingDao
				.getTransactionsByArCustomerIdandStatus(orderGlobalVariables.getBillingCon(), customerArId, "NOT_PAID");

		List<String> invoiceIds = transacDao.stream().map(x -> x.get("TRANSACTION_ID")).collect(Collectors.toList());

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(invoiceIds, tds,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),	paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, paymentResponse.getPaymentAmount());

		Thread.sleep(2000);
		for (int i = 0; i < paymentResponse.getBillSummary().size(); i++) {
			adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(paymentResponse.getPaymentAmount().add(BigDecimal.TEN)),invoiceIds.get(i));
		}

		Thread.sleep(2000);

		InvoiceMeta invoiceMongoData = billingmongo.getInvoiceMongoCollectionByCompanyId(orderGlobalVariables.getCompanyUuid());
		if (invoiceMongoData.getStatus().toString() == "POSTING_COMPLETED") {
			ReportHelper.logValidationFailure("Invalid Invoice Status", "PAYMENT_SUCCESS",
					String.valueOf(invoiceMongoData.getStatus()), "Invalid Status");
			Assert.assertTrue(false);
		}

	}

	@Test(priority = 15)
	public void duplicateEvent() throws InterruptedException {
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(expectedPgAmount), invoiceId);

		Thread.sleep(4000);
		InvoiceMeta invoiceMongoData = billingmongo
				.getInvoiceMongoCollectionByCompanyId(orderGlobalVariables.getCompanyUuid());
		TransactionDTO transacMongoData = billingmongo.getTransacMongoCollectionByid(paymentResponse.getOrderId());

		adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(expectedPgAmount), invoiceId);
		adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(expectedPgAmount), invoiceId);
		adtechbillingApiManager.pushReceiptEventinKafka(Arrays.asList(expectedPgAmount), invoiceId);

		InvoiceMeta invoiceMongoDataAfterDuplicateEvent = billingmongo
				.getInvoiceMongoCollectionByCompanyId(orderGlobalVariables.getCompanyUuid());
		TransactionDTO transacMongoDataAfterDuplicateEvent = billingmongo
				.getTransacMongoCollectionByid(paymentResponse.getOrderId());

		commonApi.compareObjectswithObjectMapper(invoiceMongoData, invoiceMongoDataAfterDuplicateEvent, null);
		commonApi.compareObjectswithObjectMapper(transacMongoData, transacMongoDataAfterDuplicateEvent, null);
	}
}
