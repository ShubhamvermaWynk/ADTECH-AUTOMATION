package com.airtel.dao.billing;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface BillingDao {
    Map<String, String> getPartnershipTypeByChannelAndEngagement(Connection connection, String channel, String engagement);
    Map<String, String> getPartnershipTypeByChannelEngagementAndBusinessContext(Connection connection, String channel, String engagement, String businessContext);
    Map<String, String> getCustomerInfoAndNotArchived(Connection connection, String companyUuid);
    List<Map<String, String>> getPendingTransactionReceiptByCustInfoId(Connection connection, String customerInfoId);
    List<Map<String, String>> getReceiptByCustInfoId(Connection connection, String customerInfoId);
    boolean updatePendingReceiptTransactionType(Connection connection, String customerInfoId, String type);
    boolean deleteJobInfoByArCustomerId(Connection connection, String customerArId);
    boolean emptyTransactionTable(Connection connection);
    boolean emptyTransactionInstallmentTable(Connection connection);
    boolean emptyReceiptTable(Connection connection);
    List<Map<String, String>> getTransactionByArCustomerId(Connection connection, String arCustomerId);
    List<Map<String, String>> getInstallmentsByTransactionId(Connection connection, String transactionId);
    List<Map<String, String>> getAllTransactionsByArCustomerId(Connection connection, String arCustomerId);
    List<Map<String, String>> getTransactionsByArCustomerId(Connection connection, String arCustomerId);
    List<Map<String, String>> getTransactionsByArCustomerIdAndTransactionClass(Connection connection, String arCustomerId, String transactionClass);
    List<Map<String, String>> getAllReceiptsByArCustomerId(Connection connection, String arCustomerId);
    List<Map<String, String>> getReceiptByArCustomerId(Connection connection, String arCustomerId);
    List<Map<String, String>> getTransactionReceipt(Connection connection);
    List<Map<String, String>> getTransactionReceiptOrderByReceiptId(Connection connection);
    List<Map<String, String>> getCreditMemo(Connection connection);
    Map<String, String> getJobInfoByArCustomerIdAndJobName(Connection connection, String arCustomerId,String jobName);
    Map<String, String> getJobInfoByArCustomerId(Connection connection, String arCustomerId);
    boolean updateTransactionByTransactionId(Connection connection, String transactionId);
    boolean updateTransactionInstallmentFlagsByTransactionId(Connection connection, String transactionId);
    boolean updateTransactionByArCustomerId(Connection connection, String customerArId);
    boolean updateReceiptByArCustomerId(Connection connection, String customerArId);
    boolean updateReceiptByArCustomerId(Connection connection, String customerArId, String receiptId);
    boolean updateReceiptToBePickedByArCustomerId(Connection connection, String customerArId, String receiptId);
    boolean updateReceiptByReceiptId(Connection connection, String transactionId);
    boolean updateReceipt(Connection connection, String receiptId);
    boolean updateIsReceiptsExistFlagByTransactionId(Connection connection, String transactionId);
    Map<String, String> getTransactionByTransactionId(Connection connection, String transactionId);
    Map<String, String> getReceiptByReceiptId(Connection connection, String transactionId);
    boolean deleteTransactionReceiptByTransactionId(Connection connection, String transactionId);
    boolean emptyTransactionReceiptTable(Connection connection);
    boolean emptyCreditMemoTable(Connection connection);
    boolean deleteTransactionReceiptByReceiptId(Connection connection, String receiptId);
    boolean deleteTransactionReceiptForRestTransactionIds(Connection connection, String transactionId);
    boolean deleteCreditMemoByTransactionId(Connection connection, String transactionId);
    boolean deleteReceiptByReceiptId(Connection connection, String receiptId);
    boolean deleteTransactionByTransactionId(Connection connection, String transactionId);
    boolean openPendingReceiptByCustomerInfoId(Connection connection, String customerInfoId);
    Map<String, String> fetchIdFromTransactionByTransactionId(Connection connection, String transactionId);
    Map<String, String> fetchIdFromReceiptByReceiptId(Connection connection, String transactionId);
    boolean updateCompanybyArId(Connection connection, String customerArId,String companyUuid);
    List<Map<String, String>> getTransactionsByArCustomerIdandStatus(Connection connection, String arCustomerId, String status);
    List<Map<String, String>> getTransactionByArCustomerIdandINV(Connection connection, String arCustomerId);
    List<Map<String, String>> getReceiptByArCustomerIdwithArchiveFlag(Connection connection, String arCustomerId);
    public boolean updateTransactionByTransactiontype(Connection connection, String transactionType, String customerArId);
    boolean updateBillingConfigurationByPartnershipTypeId(Connection connection, String partnershipTypeId, String refetchInvoiceUrlFlag);
    boolean updateDocumentUrlInTransactionByTransactionId(Connection connection, String transactionId);

}
