package com.airtel.common.variables;

import java.util.Properties;

import com.airtel.common.dto.request.DbRequestDto;
import com.airtel.teams.common.CommonApi;
import  com.airtel.adtech.constants.enums.DbType;
import org.apache.commons.lang3.RandomStringUtils;

public class InitOrderVariables extends OrderGlobalVariables {
	private CommonApi commonApi = new CommonApi();

    public OrderGlobalVariables initializeOrderVariables(Properties propertyFile)
    {
        OrderGlobalVariables orderGlobalVariables=new OrderGlobalVariables();

//        mongoserverInitials=propertyFile.getProperty("MONGO_CREDENTAILS");
//        orderGlobalVariables.setMongoserverInitials(mongoserverInitials);
        
        mongoMultiserverInitials=propertyFile.getProperty("MONGO_CREDENTAILS_CLUSTER").split(",");
        orderGlobalVariables.setMongoMultiserverInitials(mongoMultiserverInitials);

        mongoport = Integer.parseInt(propertyFile.getProperty("PORT"));
        orderGlobalVariables.setMongoport(mongoport);

        orderdatabaseInitials=propertyFile.getProperty("TEAM_ORDER_DATABASE_NAME");
        orderGlobalVariables.setOrderdatabaseInitials(orderdatabaseInitials);

        orderCollection= propertyFile.getProperty("ORDER_COLLECTION_NAME");
        orderGlobalVariables.setOrderCollection(orderCollection);

        orderContextCollection=propertyFile.getProperty("ORDER_CONTEXT_COLLECTION_NAME");
        orderGlobalVariables.setOrderContextCollection(orderContextCollection);

        orderMappingCollection= propertyFile.getProperty("MACHINE_MAPPING_COLLECTION_NAME");
        orderGlobalVariables.setOrderMappingCollection(orderMappingCollection);

        honchodatabaseInitials= propertyFile.getProperty("HONCHO_DATABASE_NAME");
        orderGlobalVariables.setHonchodatabaseInitials(honchodatabaseInitials);

        automataCollection=propertyFile.getProperty("AUTOMATA_COLLECTION_NAME");
        orderGlobalVariables.setAutomataCollection(automataCollection);

        automataContextCollection=propertyFile.getProperty("AUTOMATA_CONTEXT_COLLECTION_NAME");
        orderGlobalVariables.setAutomataContextCollection(automataContextCollection);

        bundleId=propertyFile.getProperty("BUNDLE_ID");
        orderGlobalVariables.setBundleId(bundleId);

        globalGstin=propertyFile.getProperty("GLOBAL_GSTIN");
        orderGlobalVariables.setGlobalGstin(globalGstin);

        browser1=propertyFile.getProperty("BROWSER_1");
        orderGlobalVariables.setBrowser1(browser1);

        machine1=propertyFile.getProperty("MACHINE_1");
        orderGlobalVariables.setMachine1(machine1);

        orderServerInitials=propertyFile.getProperty("TEAM_ORDER_URL");
        orderGlobalVariables.setOrderServerInitials(orderServerInitials);

        onboardingServerInitials=propertyFile.getProperty("USERS_URL");
        orderGlobalVariables.setOnboardingServerInitials(onboardingServerInitials);

        sidCommon=propertyFile.getProperty("COMMON_SID");
        orderGlobalVariables.setSidCommon(sidCommon);

        sidIam=propertyFile.getProperty("IAM_SID");
        orderGlobalVariables.setSidIam(sidIam);

        sid=propertyFile.getProperty("MYSQLDB_SID");
        orderGlobalVariables.setSid(sid);

        billingSchema=propertyFile.getProperty("BILLING_SID");
        orderGlobalVariables.setBillingSchema(billingSchema);

        port=propertyFile.getProperty("MYSQLDB_PORT");
        orderGlobalVariables.setPort(port);

        dbIp=propertyFile.getProperty("MYSQLDB_IP");
        orderGlobalVariables.setDbIp(dbIp);

        dbUsername=propertyFile.getProperty("MYSQLDB_USER");
        orderGlobalVariables.setDbUsername(dbUsername);

        dbPassword=propertyFile.getProperty("MYSQLDB_PASSWORD");
        orderGlobalVariables.setDbPassword(dbPassword);

        mobileNumber=propertyFile.getProperty("MOBILE_NO");
        orderGlobalVariables.setMobileNumber(mobileNumber);

        firstName=propertyFile.getProperty("FIRST_NAME");
        orderGlobalVariables.setFirstName(firstName);

        lastName=propertyFile.getProperty("LAST_NAME");
        orderGlobalVariables.setLastName(lastName);

        signupPassword=propertyFile.getProperty("SIGNUP_PASSWORD");
        orderGlobalVariables.setSignupPassword(signupPassword);

        emailId1=propertyFile.getProperty("SIGNUP_EMAIL_1");
        orderGlobalVariables.setEmailId1(emailId1);

        positiveAppName=propertyFile.getProperty("POSITIVE_APP_NAME");
        orderGlobalVariables.setPositiveAppName(positiveAppName);

        identityServerInitials=propertyFile.getProperty("TEAMS_AUTHENTICATOR_URL");
        orderGlobalVariables.setIdentityServerInitials(identityServerInitials);

        String panPathName=propertyFile.getProperty("PAN_PATHNAME");
        orderGlobalVariables.setPanPathName(panPathName);

        
       
        
        
        uniqueidentifier = CommonApi.uniqueidentifier();
        orderGlobalVariables.setUniqueidentifier(uniqueidentifier);

        pgTxnId= RandomStringUtils.randomNumeric(10)+RandomStringUtils.randomNumeric(10);
        orderGlobalVariables.setPgTxnId(pgTxnId);

        com.airtel.common.dto.request.DbRequestDto Common_dbRequestDTO = new DbRequestDto(dbIp, dbUsername, dbPassword, port, sidCommon);
        testConCommon = commonApi.createDbConnection(Common_dbRequestDTO, DbType.MYSQL);
        orderGlobalVariables.setTestConCommon(testConCommon);

        com.airtel.common.dto.request.DbRequestDto Iam_dbRequestDTO = new DbRequestDto(dbIp, dbUsername, dbPassword, port, sidIam);
        testConIam = commonApi.createDbConnection(Iam_dbRequestDTO, DbType.MYSQL);
        orderGlobalVariables.setTestConIam(testConIam);

        DbRequestDto dbRequestDTO = new DbRequestDto(dbIp, dbUsername, dbPassword, port, sid);
        testCon = commonApi.createDbConnection(dbRequestDTO, DbType.MYSQL);
        orderGlobalVariables.setTestCon(testCon);

        DbRequestDto dbRequestDTObilling = new DbRequestDto(dbIp, dbUsername, dbPassword, port, billingSchema);
        billingCon=commonApi.createDbConnection(dbRequestDTObilling,DbType.MYSQL);
        orderGlobalVariables.setBillingCon(billingCon);
        
        
        //Adtech
        
        mobileNumberAd=propertyFile.getProperty("ADTECH_MOBILE_NO");
        orderGlobalVariables.setMobileNumberAd(mobileNumberAd);
        
        emailIdAd=propertyFile.getProperty("ADTECH_EMAIL_ID");
        orderGlobalVariables.setEmailIdAd(emailIdAd);
        
        gstinAd=propertyFile.getProperty("ADTECH_GSTIN");
        orderGlobalVariables.setGstinAd(gstinAd);
        
        return orderGlobalVariables;
    }
}
