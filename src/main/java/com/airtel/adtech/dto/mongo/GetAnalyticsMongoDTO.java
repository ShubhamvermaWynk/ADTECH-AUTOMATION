package com.airtel.adtech.dto.mongo;

import java.time.Instant;

public class GetAnalyticsMongoDTO {
	
	private Long deliveredCount=0L;
	private Long scrubbingFailedCount=0L;
	private Long customerErrorCount=0L;
	private Long systemErrorCount=0L;
	private Long failedCount=0L;
	private Long linkClickedCount=0L;
	private Long totalSmsReceivedByIq=0L;
	private Long totalSmsSentToIq=0L;
	private String campaignId;
	private Instant executionStartTime;
	private Instant executionEndTime;
	private String status;
	private int retryCount;
	
	
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public Long getDeliveredCount() {
		return deliveredCount;
	}
	public void setDeliveredCount(Long deliveredCount) {
		this.deliveredCount = deliveredCount;
	}
	public Long getScrubbingFailedCount() {
		return scrubbingFailedCount;
	}
	public void setScrubbingFailedCount(Long scrubbingFailedCount) {
		this.scrubbingFailedCount = scrubbingFailedCount;
	}
	public Long getCustomerErrorCount() {
		return customerErrorCount;
	}
	public void setCustomerErrorCount(Long customerErrorCount) {
		this.customerErrorCount = customerErrorCount;
	}
	public Long getSystemErrorCount() {
		return systemErrorCount;
	}
	public void setSystemErrorCount(Long systemErrorCount) {
		this.systemErrorCount = systemErrorCount;
	}
	public Long getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(Long failedCount) {
		this.failedCount = failedCount;
	}
	public Long getLinkClickedCount() {
		return linkClickedCount;
	}
	public void setLinkClickedCount(Long linkClickedCount) {
		this.linkClickedCount = linkClickedCount;
	}
	public Long getTotalSmsReceivedByIq() {
		return totalSmsReceivedByIq;
	}
	public void setTotalSmsReceivedByIq(Long totalSmsReceivedByIq) {
		this.totalSmsReceivedByIq = totalSmsReceivedByIq;
	}
	public Long getTotalSmsSentToIq() {
		return totalSmsSentToIq;
	}
	public void setTotalSmsSentToIq(Long totalSmsSentToIq) {
		this.totalSmsSentToIq = totalSmsSentToIq;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	
	
	public Instant getExecutionStartTime() {
		return executionStartTime;
	}
	public void setExecutionStartTime(Instant executionStartTime) {
		this.executionStartTime = executionStartTime;
	}
	public Instant getExecutionEndTime() {
		return executionEndTime;
	}
	public void setExecutionEndTime(Instant executionEndTime) {
		this.executionEndTime = executionEndTime;
	}
	@Override
	public String toString() {
		return "GetAnalyticsMongoDTO [deliveredCount=" + deliveredCount + ", scrubbingFailedCount="
				+ scrubbingFailedCount + ", customerErrorCount=" + customerErrorCount + ", systemErrorCount="
				+ systemErrorCount + ", failedCount=" + failedCount + ", linkClickedCount=" + linkClickedCount
				+ ", totalSmsReceivedByIq=" + totalSmsReceivedByIq + ", totalSmsSentToIq=" + totalSmsSentToIq
				+ ", campaignId=" + campaignId + ", executionStartTime=" + executionStartTime + ", executionEndTime="
				+ executionEndTime + ", status=" + status + ", retryCount=" + retryCount + "]";
	}

	
	

}
