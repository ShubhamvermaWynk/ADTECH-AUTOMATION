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
import com.airtel.adtech.constants.enums.TransactionStatus;
import com.airtel.adtech.dto.response.billing.InitiatePaymentResponseDTO;
import com.airtel.adtech.dto.response.billing.InvoiceListDTO;
import com.airtel.adtech.dto.response.billing.PgPaymentResponseDto;
import com.airtel.adtech.manager.BillingAdtechApiManager;
import com.airtel.adtech.manager.OnboardApiManager;
import com.airtel.adtech.mongo.billing.BillingAdMongo;
import com.airtel.adtech.mongo.billing.BillingAdMongoImpl;
import com.airtel.dao.billing.BillingDao;
import com.airtel.dao.billing.BillingDaoImpl;

import com.airtel.adtech.dto.response.billing.ReceiptByInvoiceIdDetails;

import com.airtel.helper.report.ReportHelper;


import com.airtel.teams.common.CommonApi;
import com.airtel.common.constants.enums.PaymentEvent;
import com.airtel.common.constants.enums.PaymentStatus;

import com.airtel.common.variables.InitOrderVariables;
import com.airtel.common.variables.OrderGlobalVariables;

import com.airtel.adtech.adaptor.billing.BillingAdAdaptor;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class InvoiceTest extends ReportHelper {
	CommonApi commonApi = new CommonApi();
	BillingDao billingDao;
	
	OrderGlobalVariables orderGlobalVariables;
	
	BillingAdAdaptor adaptor;
	String uniqueIdentifier;
	Connection billingCon;
	String customerArId;
	Properties billingDataPropertyFile, adtechbillingDataPropertyFile;
	
	String invoiceId, invoicePartialPaid = null;
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
		adtechbillingApiManager = new BillingAdtechApiManager(envPropertyFile, adtechbillingDataPropertyFile);

		customerArId = billingDataPropertyFile.getProperty("AR_CUSTOMER_ID");
		invoiceId = adtechbillingDataPropertyFile.getProperty("INVOICEID_NOTPAID");
		invoicePartialPaid = adtechbillingDataPropertyFile.getProperty("INVOICEID_PARTIALPAID");
		tds = new BigDecimal(adtechbillingDataPropertyFile.getProperty("TDS"));
		circleId=adtechbillingDataPropertyFile.getProperty("CIRCLE_ID");

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
	
	public void createUserCompany()
	{
		orderGlobalVariables = onboardApiManager.createUserCompanyAdtech(orderGlobalVariables);
		billingDao.updateCompanybyArId(orderGlobalVariables.getBillingCon(), customerArId,orderGlobalVariables.getCompanyUuid());
	}

	@Test(priority = 1)
	public void fetchInvoices() {

		List<InvoiceListDTO> invoiceList = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		List<Map<String, String>> list = billingDao.getTransactionByArCustomerIdandINV(orderGlobalVariables.getBillingCon(), customerArId);

		
		if (!(invoiceList.size() == list.size())) {
			ReportHelper.logValidationFailure("transactionlist", String.valueOf(list.size()),	String.valueOf(invoiceList.size()), "Incorrect transaction size");
			Assert.assertTrue(false);
		}
		
		List<InvoiceListDTO> expectedinvoiceList= adtechbillingApiManager.expectedInvoiceList(list);
		invoiceList=invoiceList.stream().sorted((t1, t2) -> t1.getInvoiceNumber().compareTo(t2.getInvoiceNumber())).collect(Collectors.toList());
		
		commonApi.compareObjects(expectedinvoiceList, invoiceList, null);
		

		adaptor.initiatePayment(invoiceIds, tds, orderGlobalVariables.getCompanyUuid(),orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		List<InvoiceListDTO> invoiceListAfter = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		
		invoiceListAfter=invoiceListAfter.stream().sorted((t1, t2) -> t1.getInvoiceNumber().compareTo(t2.getInvoiceNumber())).collect(Collectors.toList());
		
		commonApi.compareObjects(invoiceListAfter, invoiceList, null);
	}

	@Test(priority = 2)
	public void fetchInvoiceAfterValidatePayment() {	

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);

		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getBillSummary().get(0).getPayableAmount(), circleId, 200, uniqueIdentifier);

		List<InvoiceListDTO> invoiceList = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);

		String actualStatus = invoiceList.stream().filter(x -> x.getInvoiceId().contains(invoiceId))
				.map(z -> z.getStatus()).findFirst().get();

		if (!actualStatus.equals("IN_PROCESS"))

		{
			ReportHelper.logValidationFailure("Fetch Invoice ", "IN_PROCESS", actualStatus,
					"incorrect status in fetch invoice");
			Assert.assertTrue(false);
		}
	}

	@Test(priority = 3)
	public void fetchInvoiceForInvalidPayment() {

		List<InvoiceListDTO> invoiceList = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);

		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				new BigDecimal(6767), circleId, 200, uniqueIdentifier);

		List<InvoiceListDTO> invoiceListAfter = adaptor.getInvoiceDetails(
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		commonApi.compareObjects(invoiceList, invoiceListAfter, null);

	}

	@Test(priority = 4)
	public void fetchInvoiceWithoutCloseFlag() throws InterruptedException {
		
		billingmongo.updateCloseFlag(invoiceId);
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId,	paymentResponse.getBillSummary().get(0).getPayableAmount());
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_SUCCESS, paymentResponse.getOrderId(),PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId,paymentResponse.getBillSummary().get(0).getPayableAmount());

		Thread.sleep(2000);
		List<InvoiceListDTO> invoiceList = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);

		String actualStatus = invoiceList.stream().filter(x -> x.getInvoiceId().contains(invoiceId))
				.map(z -> z.getStatus()).findFirst().get();
		if (!actualStatus.equals("IN_PROCESS")) {
			ReportHelper.logValidationFailure("Invoice status", "IN_PROCESS", actualStatus,
					"incorrect status in fetch invoice");
			Assert.assertTrue(false);

		}

	}

	

	@Test(priority = 6)
	public void fetchInvoiceAfterFailurePayment() throws InterruptedException {
		List<InvoiceListDTO> invoiceList = adaptor.getInvoiceDetails(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		BigDecimal expectedPgAmount = paymentResponse.getBillSummary().get(0).getPayableAmount();
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_PENDING, paymentResponse.getOrderId(),
				PaymentEvent.PRE_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);
		adtechbillingApiManager.produceEvent(PaymentStatus.PAYMENT_FAILED, paymentResponse.getOrderId(),
				PaymentEvent.POST_PAYMENT, paymentResponse.getLobName(), pgId, expectedPgAmount);

		Thread.sleep(2000);
		
		List<InvoiceListDTO> invoiceListAfter = adaptor.getInvoiceDetails(
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		commonApi.compareObjects(invoiceList, invoiceListAfter, null);
	}

	@Test(priority = 7)
	public void fetchReceipts() {
		List<ReceiptByInvoiceIdDetails> receiptList = adaptor.getRecieptDetails(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), null, uniqueIdentifier);
		int expectedResult = billingDao
				.getReceiptByArCustomerIdwithArchiveFlag(orderGlobalVariables.getBillingCon(), customerArId).size();
		if (!(receiptList.size() == expectedResult)) {
			ReportHelper.logValidationFailure("Reciepts size ", String.valueOf(expectedResult),
					String.valueOf(receiptList.size()), "Mismatch in receipts size");
			Assert.assertTrue(false);

		}
	}

	@Test(priority = 8)
	public void fetchTransaction() {
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validatePayment(paymentResponse.getOrderId(), paymentResponse.getLobName(),
				paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);

		List<PgPaymentResponseDto> fetchtransac = adaptor.fetchTransaction(orderGlobalVariables.getCompanyUuid(),
				orderGlobalVariables.getUserUUid(), uniqueIdentifier);

	
		if (!fetchtransac.get(0).getStatus().name().equals("VALIDATED")) {
			ReportHelper.logValidationFailure("Fetch transaction", TransactionStatus.VALIDATED.name(),
					fetchtransac.get(0).getStatus().toString(), "incorrect status in fetch transaction");
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority = 9)
	public void verifyTdsNegValue()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), new BigDecimal(-7.20),orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);
		
	}
	
	@Test(priority = 10)
	public void verifyTdsuptoThreeDecimcalValue()
	{
		adaptor.initiatePayment(Arrays.asList(invoiceId), new BigDecimal(7.876),orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);
		
	}
	
	@Test(priority = 11)
	public void verifyTdsGreaterThanTenValue()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), new BigDecimal(14.00),orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);
		
	}
	
	@Test(priority = 12)
	public void verifyTdsuptoTwoDecimcalValue()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), new BigDecimal(7.87).setScale(2, RoundingMode.HALF_UP),orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		
	}
	@Test(priority = 13)
	public void verifyTdsasNullValue()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), null,orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 400, uniqueIdentifier);
		 
	}
	
	@Test(priority = 14)
	public void verifyValidateAdtechBillingApi()
	{
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validateTransaction(paymentResponse.getOrderId(),paymentResponse.getLobName() , paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);
		
	}
	
	@Test(priority = 15)
	public void verifyTransactionStatus()
	{
		InitiatePaymentResponseDTO paymentResponse = adaptor.initiatePayment(Arrays.asList(invoiceId), tds,
				orderGlobalVariables.getCompanyUuid(), orderGlobalVariables.getUserUUid(), 200, uniqueIdentifier);
		adaptor.validateTransaction(paymentResponse.getOrderId(),paymentResponse.getLobName() , paymentResponse.getPaymentAmount(), circleId, 200, uniqueIdentifier);
		
		Response response= adaptor.getStatus(paymentResponse.getOrderId(), 200, uniqueIdentifier);
		JSONObject json= new JSONObject(response.asString());
		String actualStatus= json.getString("status");
		
		if (!actualStatus.equals("VALIDATED")) {
			ReportHelper.logValidationFailure("Transaction Status ", "VALIDATED",actualStatus, "Mismatch in status");
			Assert.assertTrue(false);

		}
		
	}
	
	@Test(priority = 16)
	public void verifyInitiatewithIncorrectCompany()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), tds,"dummycompany", orderGlobalVariables.getUserUUid(), 403, uniqueIdentifier);
		
	}
	
	@Test(priority = 17)
	public void verifyInitiatewithIncorrectUserId()
	{
		 adaptor.initiatePayment(Arrays.asList(invoiceId), tds, orderGlobalVariables.getCompanyUuid(), "dummyuser", 400, uniqueIdentifier);
	}
	@Test(priority = 18)
	public void verifyInitiatewithoutUser()
	{
		adaptor.initiatePayment(Arrays.asList(invoiceId), tds,orderGlobalVariables.getCompanyUuid(),null, 400, uniqueIdentifier);
		
		
	}

}
