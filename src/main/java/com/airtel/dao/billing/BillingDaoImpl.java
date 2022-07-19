package com.airtel.dao.billing;

import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;
import org.testng.Assert;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class BillingDaoImpl extends CommonApi implements BillingDao {

    @Override
    public Map<String, String> getPartnershipTypeByChannelAndEngagement(Connection connection, String channel,String engagement) {

        String query = null;

        query = "SELECT * FROM PARTNERSHIP_TYPE where CHANNEL='"+channel+"' AND ENGAGEMENT='"+engagement+"'";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        if(queryData.size()!=1)
        {
            ReportHelper.logValidationFailure("partnership type size","1",String.valueOf(queryData.size()),"partnership type size doesn't match");
            Assert.assertTrue(false);
        }

        return queryData.get(0);
    }

    @Override
    public Map<String, String> getPartnershipTypeByChannelEngagementAndBusinessContext(Connection connection, String channel, String engagement, String businessContext) {

        String query = "SELECT * FROM PARTNERSHIP_TYPE where CHANNEL='"+channel+"' AND ENGAGEMENT='"+engagement+"' AND BUSINESS_CONTEXT='"+businessContext+"'";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        if(queryData.size()!=1)
        {
            ReportHelper.logValidationFailure("partnership type size","1",String.valueOf(queryData.size()),"partnership type size doesn't match");
            Assert.assertTrue(false);
        }

        return queryData.get(0);
    }

    @Override
    public Map<String, String> getCustomerInfoAndNotArchived(Connection connection, String companyUuid) {

        String query = null;

        query = "SELECT * FROM CUSTOMER_INFO where COMPANY_UUID='"+companyUuid+"' AND IS_ARCHIVED=0";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        if(queryData.size()!=1)
        {
            ReportHelper.logValidationFailure("customer info size","1",String.valueOf(queryData.size()),"customer info size doesn't match");
            Assert.assertTrue(false);
        }
        return queryData.get(0);
    }

    @Override
    public List<Map<String, String>> getPendingTransactionReceiptByCustInfoId(Connection connection, String customerInfoId) {

        String query = null;

        query = "SELECT * FROM PENDING_RECEIPT_REQUEST where CUSTOMER_INFO_ID="+customerInfoId;

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getReceiptByCustInfoId(Connection connection, String customerInfoId) {

        String query = null;

        query = "SELECT * FROM RECEIPT where CUSTOMER_INFO_ID="+customerInfoId+" AND IS_ARCHIVED=0";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public boolean updatePendingReceiptTransactionType(Connection connection, String customerInfoId, String type) {
        String query = "update PENDING_RECEIPT_REQUEST set TRANSACTION_TYPE='"+type+"' where CUSTOMER_INFO_ID="+customerInfoId;

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating pending receipt request type", "DB connection", "error in db connection",
                    "error while updating pending receipt request type");
            Assert.assertTrue(false);
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteJobInfoByArCustomerId(Connection connection, String customerArId) {
        String query = "delete from JOB_INFO where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+customerArId+")";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting job_info", "DB connection", "error in db connection",
                    "error while deleting job info");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean emptyTransactionTable(Connection connection) {
        String query = "delete from TRANSACTION";
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction", "DB connection", "error in db connection",
                    "error while deleting transaction");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean emptyTransactionInstallmentTable(Connection connection) {
        String query = "delete from INSTALLMENT";
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting installments", "DB connection", "error in db connection",
                    "error while deleting installments");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean emptyReceiptTable(Connection connection) {
        String query = "delete from RECEIPT";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction", "DB connection", "error in db connection",
                    "error while deleting transaction");
            Assert.assertTrue(false);
            return false;
        }

        return true;
    }

    @Override
    public List<Map<String, String>> getTransactionByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND TRANSACTION_ID IN (841301,841302,841303) order by TRANSACTION_ID";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getAllTransactionsByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") order by TRANSACTION_ID";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getTransactionsByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND STATUS='PAID' AND TRANSACTION_CLASS='INV' order by TRANSACTION_ID";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getInstallmentsByTransactionId(Connection connection, String transactionId) {

        String query = "SELECT * FROM INSTALLMENT where TRANSACTION_ID="+transactionId;
        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);
        return queryData;
    }

    @Override
    public List<Map<String, String>> getTransactionsByArCustomerIdAndTransactionClass(Connection connection, String arCustomerId, String transactionClass) {

        String query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND TRANSACTION_CLASS='INV'";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getAllReceiptsByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM RECEIPT where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND RECEIPT_ID IN (40130129,40130130,40130131) order by RECEIPT_ID";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }

    @Override
    public List<Map<String, String>> getReceiptByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM RECEIPT where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND TRANSACTION_TYPE='RECEIPT'";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData;
    }


    @Override
    public Map<String, String> getJobInfoByArCustomerIdAndJobName(Connection connection, String arCustomerId,String jobName) {

        String query = null;

        query = "SELECT * FROM JOB_INFO where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND JOB_NAME='"+jobName+"'";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        if(queryData.size()!=1)
        {
            ReportHelper.logValidationFailure("job info size","1",String.valueOf(queryData.size()),"job info size doesn't match");
            Assert.assertTrue(false);
        }

        return queryData.get(0);
    }

    @Override
    public Map<String, String> getJobInfoByArCustomerId(Connection connection, String arCustomerId) {

        String query = null;

        query = "SELECT * FROM JOB_INFO where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+")";

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        if(queryData.size()!=1)
        {
            ReportHelper.logValidationFailure("job info size","1",String.valueOf(queryData.size()),"job info size doesn't match");
            Assert.assertTrue(false);
        }

        return queryData.get(0);
    }

    @Override
    public boolean updateTransactionByTransactionId(Connection connection, String transactionId) {

        String query = "update TRANSACTION set STATUS='NOT_PAID',PENDING_AMOUNT='1.00' where TRANSACTION_ID="+transactionId;
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating transaction", "DB connection", "error in db connection",
                    "error while updating transaction");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateTransactionInstallmentFlagsByTransactionId(Connection connection, String transactionId) {

        String query = "update TRANSACTION set FETCH_INSTALLMENTS_FLAG=1, INSTALLMENTS_NUMBER_TYPE='NO' where TRANSACTION_ID IN(" + transactionId + ")";
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating FETCH_INSTALLMENTS_FLAG", "DB connection", "error in db connection",
                    "error while updating FETCH_INSTALLMENTS_FLAG");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateReceiptByReceiptId(Connection connection, String receiptId) {
        String query = "update RECEIPT set RECEIPT_STATUS='NOT_APPLIED' where RECEIPT_ID="+receiptId;

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating receipt", "DB connection", "error in db connection",
                    "error while updating receipt");
            Assert.assertTrue(false);
            return false;
        }

        return true;
    }

    @Override
    public Map<String, String> getTransactionByTransactionId(Connection connection, String transactionId) {

        String query = "SELECT * FROM TRANSACTION where TRANSACTION_ID="+transactionId;

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData.get(0);
    }

    @Override
    public boolean updateTransactionByArCustomerId(Connection connection, String arCustomerId) {

        String query = "update TRANSACTION set PENDING_AMOUNT='0.00',STATUS='PAID',IS_RECEIPTS_EXIST=0,TRANSACTION_CLASS='INV',TRANSACTION_TYPE='INV - Prepaid Dist.' where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND STATUS='PAID' AND TRANSACTION_ID='841301'";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating transaction", "DB connection", "error in db connection",
                    "error while updating transaction");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateReceiptByArCustomerId(Connection connection, String arCustomerId, String receiptId) {

        String query = "update RECEIPT set IS_INVOICE_EXIST=1,IS_ARCHIVED=1 where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND RECEIPT_ID != '"+receiptId+"'";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating Receipt table", "DB connection", "error in db connection",
                    "error while updating Receipt table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateReceiptByArCustomerId(Connection connection, String arCustomerId) {

        String query = "update RECEIPT set IS_INVOICE_EXIST=0,IS_ARCHIVED=0 where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+")";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating Receipt table", "DB connection", "error in db connection",
                    "error while updating Receipt table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateReceiptToBePickedByArCustomerId(Connection connection, String arCustomerId, String receiptId) {

        String query = "update RECEIPT set IS_INVOICE_EXIST=0 where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND RECEIPT_ID = '"+receiptId+"'";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating Receipt table", "DB connection", "error in db connection",
                    "error while updating Receipt table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateReceipt(Connection connection, String receiptId) {

        String query = "update RECEIPT set IS_INVOICE_EXIST=0 where RECEIPT_ID='"+receiptId+"'";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating Receipt table", "DB connection", "error in db connection",
                    "error while updating Receipt table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateIsReceiptsExistFlagByTransactionId(Connection connection, String transactionId) {

        String query = "update TRANSACTION set IS_RECEIPTS_EXIST=0 where TRANSACTION_ID='"+transactionId+"'";

        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating Transaction table", "DB connection", "error in db connection",
                    "error while updating Transaction table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public Map<String, String> getReceiptByReceiptId(Connection connection, String receiptId) {

        String query = "SELECT * FROM RECEIPT where RECEIPT_ID="+receiptId;

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData.get(0);
    }

    @Override
    public boolean emptyTransactionReceiptTable(Connection connection) {
        String query = "delete from TRANSACTION_RECEIPT";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction receipt", "DB connection", "error in db connection",
                    "error while deleting transaction receipt");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }
    @Override
    public boolean deleteTransactionReceiptByTransactionId(Connection connection, String transactionId) {
        String query = "delete from TRANSACTION_RECEIPT where TRANSACTION_ID IN(" + transactionId + ")";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction receipt", "DB connection", "error in db connection",
                    "error while deleting transaction receipt");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean emptyCreditMemoTable(Connection connection) {
        String query = "delete from CREDIT_MEMO";
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting credit memo table", "DB connection", "error in db connection",
                    "error while deleting credit memo table");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTransactionReceiptForRestTransactionIds(Connection connection, String transactionId) {
        String query = "delete from TRANSACTION_RECEIPT where TRANSACTION_ID!=" + transactionId;

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction receipt", "DB connection", "error in db connection",
                    "error while deleting transaction receipt");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteCreditMemoByTransactionId(Connection connection, String transactionId) {
        String query = "delete from CREDIT_MEMO where INVOICE_ID IN (" + transactionId + ")";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting credit memo", "DB connection", "error in db connection",
                    "error while deleting credit memo");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTransactionReceiptByReceiptId(Connection connection, String receiptId) {
        String query = "delete from TRANSACTION_RECEIPT where RECEIPT_ID IN(" + receiptId + ")";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting transaction receipt", "DB connection", "error in db connection",
                    "error while deleting transaction receipt");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteReceiptByReceiptId(Connection connection, String receiptId) {
        String query = "delete from RECEIPT where RECEIPT_ID='" + receiptId + "'";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting receipt", "DB connection", "error in db connection",
                    "error while deleting receipt");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTransactionByTransactionId(Connection connection, String transactionId) {
        String query = "delete from TRANSACTION where TRANSACTION_ID IN(" + transactionId + ")";

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while deleting Transaction", "DB connection", "error in db connection",
                    "error while deleting Transaction");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public boolean openPendingReceiptByCustomerInfoId(Connection connection, String customerInfoId) {
        String query = "update PENDING_RECEIPT_REQUEST set CLOSED=0 where CUSTOMER_INFO_ID="+customerInfoId;

        int result = updateDeleteDataFromDb(connection, query, false);

        if (result == -1) {
            ReportHelper.logValidationFailure("error while opening pending receipt req", "DB connection", "error in db connection",
                    "error while opening pending receipt req");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String, String>> getTransactionReceiptOrderByReceiptId(Connection connection) {
        String query = "SELECT * FROM TRANSACTION_RECEIPT order by RECEIPT_ID";
        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);
        return queryData;
    }

    @Override
    public List<Map<String, String>> getTransactionReceipt(Connection connection) {
        String query = "SELECT * FROM TRANSACTION_RECEIPT";
        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);
        return queryData;
    }

    @Override
    public List<Map<String, String>> getCreditMemo(Connection connection) {
        String query = "SELECT * FROM CREDIT_MEMO";
        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);
        return queryData;
    }

    @Override
    public Map<String, String> fetchIdFromTransactionByTransactionId(Connection connection, String transactionId) {

        String query = "SELECT ID FROM TRANSACTION where TRANSACTION_ID=" + transactionId;

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData.get(0);
    }

    @Override
    public Map<String, String> fetchIdFromReceiptByReceiptId(Connection connection, String receiptId) {

        String query = "SELECT ID FROM RECEIPT where RECEIPT_ID=" + receiptId;

        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

        return queryData.get(0);
    }

	@Override
	public boolean updateCompanybyArId(Connection connection, String customerArId, String companyUuid) {
		
		try {
			String query = "update CUSTOMER_INFO set company_uuid='" + companyUuid + "' where AR_ID="+customerArId;

			int result = updateDeleteDataFromDb(connection, query, false);

			if (result == -1) {
			    ReportHelper.logValidationFailure("error while updating companyId ", "DB connection", "error in db connection",
			            "error while updating company uuid");
			    Assert.assertTrue(false);
			    return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
	}
    
	 @Override
     public List<Map<String, String>> getTransactionsByArCustomerIdandStatus(Connection connection, String arCustomerId, String status) {

	        String query = null;

	        query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND STATUS='"+status+"' AND TRANSACTION_CLASS='INV'";

	        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);
	        
			return queryData;
	 }
	 
	 @Override
     public List<Map<String, String>> getTransactionByArCustomerIdandINV(Connection connection, String arCustomerId) {

	        String query = null;

	        query = "SELECT * FROM TRANSACTION where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND TRANSACTION_CLASS='INV' order by TRANSACTION_ID";

	        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

	        return queryData;
    }
	 
    @Override
    public List<Map<String, String>> getReceiptByArCustomerIdwithArchiveFlag(Connection connection, String arCustomerId) {

	        String query = null;

	        query = "SELECT * FROM RECEIPT where CUSTOMER_INFO_ID=(select ID from CUSTOMER_INFO where AR_ID="+arCustomerId+") AND IS_ARCHIVED='0' AND TRANSACTION_TYPE='RECEIPT'";

	        List<Map<String, String>> queryData = fetchDatafromDb(connection, query);

	        return queryData;
    }

    @Override
    public boolean updateTransactionByTransactiontype(Connection connection, String transactionType, String customerArId) {
	        String query = "update TRANSACTION set STATUS='NOT_PAID',PENDING_AMOUNT='1.00' where TRANSACTION_CLASS= '"+transactionType+ "' AND CUSTOMER_INFO_ID=" +customerArId;

	        int result = updateDeleteDataFromDb(connection, query, false);

	        if (result == -1) {
	            ReportHelper.logValidationFailure("error while updating transaction", "DB connection", "error in db connection",
	                    "error while updating transaction");
	            Assert.assertTrue(false);
	            return false;
	        }

	        return true;
    }

    @Override
    public boolean updateBillingConfigurationByPartnershipTypeId(Connection connection, String partnershipTypeId, String refetchInvoiceUrlFlag) {

        try {
            String query = "update BILLING_CONFIGURATION set REFETCH_INVOICE_URL=" + refetchInvoiceUrlFlag + " where PARTNERSHIP_TYPE_ID=" + partnershipTypeId;

            int result = updateDeleteDataFromDb(connection, query, false);

            if (result == -1) {
                ReportHelper.logValidationFailure("error while updating companyId ", "DB connection", "error in db connection",
                        "error while updating company uuid");
                Assert.assertTrue(false);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean updateDocumentUrlInTransactionByTransactionId(Connection connection, String transactionId) {

        String query = "update TRANSACTION set DOCUMENT_URL=NULL where TRANSACTION_ID IN(" + transactionId + ")";
        int result = updateDeleteDataFromDb(connection, query, false);
        if (result == -1) {
            ReportHelper.logValidationFailure("error while updating DOCUMENT_URL to null", "DB connection", "error in db connection",
                    "error while updating DOCUMENT_URL to null");
            Assert.assertTrue(false);
            return false;
        }
        return true;
    }
}
