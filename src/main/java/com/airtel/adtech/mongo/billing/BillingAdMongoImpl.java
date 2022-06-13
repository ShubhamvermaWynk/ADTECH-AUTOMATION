package com.airtel.adtech.mongo.billing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.testng.Assert;

import com.airtel.adtech.dto.mongo.InvoiceMeta;
import com.airtel.adtech.dto.mongo.TransactionDTO;

import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;

public class BillingAdMongoImpl extends CommonApi implements BillingAdMongo {

	CommonApi commonApi = new CommonApi();
	String[] mongoMultiserverInitials;
	int mongoport;
	String addatabaseInitials, transactionCollection, invoiceCollection;

	public BillingAdMongoImpl(Properties envPropertyFile) {
		mongoMultiserverInitials = envPropertyFile.getProperty("MONGO_CREDENTAILS_CLUSTER").split(",");
		mongoport = Integer.valueOf(envPropertyFile.getProperty("PORT"));
		addatabaseInitials = envPropertyFile.getProperty("AD_BILLING_DATABASE_NAME");
		transactionCollection = envPropertyFile.getProperty("TRANSAC_COLLECTION_NAME");
		invoiceCollection = envPropertyFile.getProperty("INVOICE_COLLECTION_NAME");

	}

	@Override
	public void updateCloseFlag(String transactionId) {

		TransactionDTO transaction = new TransactionDTO();
		transaction.setClosed(true);
		String updatedMongoObj = convertToJsonwithObjectMapper(transaction);

		commonApi.updateMongoData(mongoMultiserverInitials, mongoport, addatabaseInitials, transactionCollection,
				"transactionId", transactionId, updatedMongoObj);

	}

	public TransactionDTO getTransacMongoCollectionByid(String transacId) {
		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("transactionId", transacId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, addatabaseInitials, transactionCollection);

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("transac size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		TransactionDTO transaction = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				TransactionDTO.class);

		return transaction;

	}

	public InvoiceMeta getInvoiceMongoCollectionByCompanyId(String companyUuid) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("companyUuid", companyUuid);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, addatabaseInitials, invoiceCollection);

		if (jsonObjectList.size() < 1) {
			ReportHelper.logValidationFailure("invoice Data size", "greater than or equal to 1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		InvoiceMeta invoiceData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				InvoiceMeta.class);

		return invoiceData;

	}

	public List<InvoiceMeta> getInvoiceMongoByTransId(String transacId, int size) {
		List<InvoiceMeta> invoices = new ArrayList<>();

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("transactionId", transacId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, addatabaseInitials, invoiceCollection);

		if (!(jsonObjectList.size() == size)) {
			ReportHelper.logValidationFailure("invoice Data size", String.valueOf(size),
					String.valueOf(jsonObjectList.size()), "collection value should be =1");
			Assert.assertTrue(false);
		}

		for (JSONObject jsonObject : jsonObjectList) {
			InvoiceMeta response = convertFromJsonwithObjectMapperwithclass(jsonObject.toString(), InvoiceMeta.class);
			invoices.add(response);
		}
		return invoices;

	}

	public InvoiceMeta getInvoiceMongoCollectionByInvoiceId(String invoiceId) {

		Map<String, String> mongoQuery = new HashMap<>();
		mongoQuery.put("invoiceId", invoiceId);

		List<JSONObject> jsonObjectList = getdatafrommongowithObjectmapper(mongoMultiserverInitials, mongoport,
				mongoQuery, addatabaseInitials, invoiceCollection);

		if (jsonObjectList.size() != 1) {
			ReportHelper.logValidationFailure("invoice Data size", "1", String.valueOf(jsonObjectList.size()),
					"collection value should be =1");
			Assert.assertTrue(false);
		}

		InvoiceMeta invoiceData = convertFromJsonwithObjectMapperwithclass(jsonObjectList.get(0).toString(),
				InvoiceMeta.class);

		return invoiceData;

	}

	@Override
	public void deleteInvoices(List<String> invoices) {

		for (String invoice : invoices) {
			deletefrommongo(mongoMultiserverInitials, mongoport, "invoiceId", invoice, addatabaseInitials,
					invoiceCollection);
		}

	}

}