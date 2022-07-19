package com.airtel.adtech.constants;

public interface AdtechApiConstants {

	final static String FETCH_INVOICE_BILLING = "/adtech-billing/invoice/v1/";
	final static String FETCH_RECEIPT_BILLING = "/adtech-billing/receipt/v1/";
	final static String INITIATE_PAYMENT_BILLING = "/adtech-billing/transaction/v1/";
	final static String INITIATE_TRANSACTION_BILLING = "/adtech-billing/transaction/v1/";
	final static String VALIDATE_BILLING = "/adtech-billing/transaction/v1/validate";
	final static String TRANSACTION_STATUS = "/adtech-billing/transaction/v1/status";
	final static String INVOICE_DETAILS_MAPPER_FILEPATH = "src/test/java/com/airtel/RestAssured/Adtech/test_data/invoiceMeta.properties";

	final static String COMMON_DLT = "/adtech-sms/dlt/";
	final static String FETCH_INDUSTRY_SMS = "/adtech-sms/targeting/industry";
	final static String SUBMIT_CAMPAIGN = "/adtech-sms/campaign/";
	final static String FETCH_TARGETING = "/adtech-sms/campaign/targeting/estimates";
	final static String FETCH_ORDER_LIST_SMS = "/adtech-sms/campaign/list/";
	final static String FETCH_ANALYTICS = "/adtech-sms/campaign/analytics";

//	Test SMS API
	final static String TEST_SMS_PATH = "/adtech-sms/test-sms/send/";

	
	final static String FETCH_AUDIENCE= "/audience-manager/audience/";
	final static String GET_AUDIENCE_COUNT="/audience-manager/affinity/audienceCount";
	
	//Admin APi
	final static String APPROVE_DLT= "/adtech-sms/admin/register/dlt";
	final static String APPROVE_CAMPAIGN= "/adtech-sms/admin/approve/campaign";
	final static String APPROVE_CAMPAIGN_BYID= "/adtech-sms/admin/approve/campaign/id";
	
	
	//Admin APi Portal
	final static String FETCH_ORDERLIST_ADMIN= "/adtech-sms/admin/orderlist";
	final static String TAG_PEID= "/adtech-sms/admin/tag/dlt/details";
	final static String NOTIFY_USER= "/adtech-sms/admin/notify/user";
	final static String FETCH_DLT_ADMIN= "/adtech-sms/admin/fetch/partner/details";
	final static String FETCH_CAMPAIGN_ADMIN= "/adtech-sms/admin/campaign";
	final static String REVISE_DLT= "/adtech-sms/admin/dlt/revise";
	final static String REVISE_CAMPAIGN= "/adtech-sms/admin/campaign/revise";
	final static String APPROVE_CAMPAIGN_ADMIN= "/adtech-sms/admin/campaign/approve";
	final static String FETCH_ANALYTICS_ADMIN= "/adtech-sms/admin/fetch/statistics";
	final static String DOWNLOAD_FILE= "/adtech-sms/admin/downloadFile";
	

	//Onboard user and Company
	final static String SIGN_UP_PATH="/team-iam/v1/signup/user";
	final static String REGISTER_COMPANY_PATH= "/team-onboarding/company/kyc/documents/";
	
	
	final static String FETCH_COMPANY_PATH="/team-onboarding/company/v1/fetch";
	final static String FETCH_COMPANY_BY_UUID_PATH="/team-onboarding/v1/user/company/fetch";
	final static String FETCH_USER_BY_UUID_PATH="/team-onboarding/user/v1/fetch";
	final static String  GET_PUBLIC_KEY="";
	final static String VALIDATE_PAYMENT_PATH="";
	
	

}
