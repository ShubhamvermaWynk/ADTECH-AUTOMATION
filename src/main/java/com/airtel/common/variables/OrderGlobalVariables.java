package com.airtel.common.variables;




import java.sql.Connection;
import java.util.Map;


public class OrderGlobalVariables {

    String orderServerInitials;
    String mongoserverInitials;
    String orderdatabaseInitials;
    String orderCollection;
    String userUUid;
    String orderContextCollection;
    String orderMappingCollection;
    String honchodatabaseInitials;
    String automataCollection;
    String automataContextCollection;
    String uniqueidentifier;
    String globalGstin;
    String browser1;
    String machine1;
    String sidCommon;
    String sidIam;
    String sid;
    String port;
    String dbIp;
    String dbUsername;
    String dbPassword;
    String mobileNumber;
    String firstName;
    String lastName;
    String signupPassword;
    String emailId1;
    String positiveAppName;
    String onboardingServerInitials;
    String identityServerInitials;
    String companyUuid;
    Connection testConCommon=null;
    Connection testConIam=null;
    Connection testCon=null;
    Connection billingCon;
    String billingSchema;


    String currentState;
    String terminalState;
    String orderId;
    String bundleId;
    String pgTxnId;
    int mongoport;
    String panPathName;
    Map<String,String> eventMap;
    String[] mongoMultiserverInitials;

    //Adtech
    String mobileNumberAd;
    String emailIdAd;
    String gstinAd;
    
    
    



    
    public String getMobileNumberAd() {
		return mobileNumberAd;
	}

	public void setMobileNumberAd(String mobileNumberAd) {
		this.mobileNumberAd = mobileNumberAd;
	}

	public String getEmailIdAd() {
		return emailIdAd;
	}

	public void setEmailIdAd(String emailIdAd) {
		this.emailIdAd = emailIdAd;
	}

	public String getGstinAd() {
		return gstinAd;
	}

	public void setGstinAd(String gstinAd) {
		this.gstinAd = gstinAd;
	}


	public String[] getMongoMultiserverInitials() {
		return mongoMultiserverInitials;
	}

	public void setMongoMultiserverInitials(String[] mongoMultiserverInitials) {
		this.mongoMultiserverInitials = mongoMultiserverInitials;
	}

	public String getOrderServerInitials() {
        return orderServerInitials;
    }

    public void setOrderServerInitials(String orderServerInitials) {
        this.orderServerInitials = orderServerInitials;
    }

    public String getMongoserverInitials() {
        return mongoserverInitials;
    }

    public void setMongoserverInitials(String mongoserverInitials) {
        this.mongoserverInitials = mongoserverInitials;
    }

    public String getOrderdatabaseInitials() {
        return orderdatabaseInitials;
    }

    public void setOrderdatabaseInitials(String orderdatabaseInitials) {
        this.orderdatabaseInitials = orderdatabaseInitials;
    }

    public String getOrderCollection() {
        return orderCollection;
    }

    public void setOrderCollection(String orderCollection) {
        this.orderCollection = orderCollection;
    }

    public String getUserUUid() {
        return userUUid;
    }

    public void setUserUUid(String userUUid) {
        this.userUUid = userUUid;
    }

    public String getOrderContextCollection() {
        return orderContextCollection;
    }

    public void setOrderContextCollection(String orderContextCollection) {
        this.orderContextCollection = orderContextCollection;
    }

    public String getOrderMappingCollection() {
        return orderMappingCollection;
    }

    public void setOrderMappingCollection(String orderMappingCollection) {
        this.orderMappingCollection = orderMappingCollection;
    }

    public String getHonchodatabaseInitials() {
        return honchodatabaseInitials;
    }

    public void setHonchodatabaseInitials(String honchodatabaseInitials) {
        this.honchodatabaseInitials = honchodatabaseInitials;
    }

    public String getAutomataCollection() {
        return automataCollection;
    }

    public void setAutomataCollection(String automataCollection) {
        this.automataCollection = automataCollection;
    }

    public String getAutomataContextCollection() {
        return automataContextCollection;
    }

    public void setAutomataContextCollection(String automataContextCollection) {
        this.automataContextCollection = automataContextCollection;
    }

    public String getUniqueidentifier() {
        return uniqueidentifier;
    }

    public void setUniqueidentifier(String uniqueidentifier) {
        this.uniqueidentifier = uniqueidentifier;
    }

    public String getGlobalGstin() {
        return globalGstin;
    }

    public void setGlobalGstin(String globalGstin) {
        this.globalGstin = globalGstin;
    }

    public String getBrowser1() {
        return browser1;
    }

    public void setBrowser1(String browser1) {
        this.browser1 = browser1;
    }

    public String getMachine1() {
        return machine1;
    }

    public void setMachine1(String machine1) {
        this.machine1 = machine1;
    }

    public String getSidCommon() {
        return sidCommon;
    }

    public void setSidCommon(String sidCommon) {
        this.sidCommon = sidCommon;
    }

    public String getSidIam() {
        return sidIam;
    }

    public void setSidIam(String sidIam) {
        this.sidIam = sidIam;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSignupPassword() {
        return signupPassword;
    }

    public void setSignupPassword(String signupPassword) {
        this.signupPassword = signupPassword;
    }

    public String getEmailId1() {
        return emailId1;
    }

    public void setEmailId1(String emailId1) {
        this.emailId1 = emailId1;
    }

    public String getPositiveAppName() {
        return positiveAppName;
    }

    public void setPositiveAppName(String positiveAppName) {
        this.positiveAppName = positiveAppName;
    }

    public String getOnboardingServerInitials() {
        return onboardingServerInitials;
    }

    public void setOnboardingServerInitials(String onboardingServerInitials) {
        this.onboardingServerInitials = onboardingServerInitials;
    }

    public String getIdentityServerInitials() {
        return identityServerInitials;
    }

    public void setIdentityServerInitials(String identityServerInitials) {
        this.identityServerInitials = identityServerInitials;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public Connection getTestConCommon() {
        return testConCommon;
    }

    public void setTestConCommon(Connection testConCommon) {
        this.testConCommon = testConCommon;
    }

    public Connection getTestConIam() {
        return testConIam;
    }

    public void setTestConIam(Connection testConIam) {
        this.testConIam = testConIam;
    }

    public Connection getTestCon() {
        return testCon;
    }

    public void setTestCon(Connection testCon) {
        this.testCon = testCon;
    }

    

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(String terminalState) {
        this.terminalState = terminalState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public int getMongoport() {
        return mongoport;
    }

    public void setMongoport(int mongoport) {
        this.mongoport = mongoport;
    }

    public String getPanPathName() {
        return panPathName;
    }

    public void setPanPathName(String panPathName) {
        this.panPathName = panPathName;
    }

    public Map<String, String> getEventMap() {
        return eventMap;
    }

    public void setEventMap(Map<String, String> eventMap) {
        this.eventMap = eventMap;
    }

    public Connection getBillingCon() {
        return billingCon;
    }

    public void setBillingCon(Connection billingCon) {
        this.billingCon = billingCon;
    }

    public String getBillingSchema() {
        return billingSchema;
    }

    public void setBillingSchema(String billingSchema) {
        this.billingSchema = billingSchema;
    }

    public String getPgTxnId() {
        return pgTxnId;
    }

    public void setPgTxnId(String pgTxnId) {
        this.pgTxnId = pgTxnId;
    }
}
