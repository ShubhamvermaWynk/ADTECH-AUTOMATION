package com.adtech.restAssured.tests.Authenticate;

import com.Adtech.adaptor.Authenticate.AuthenticateAdaptorImpl;
import com.Adtech.manager.AuthenticateApiManager;
import com.airtel.helper.report.ReportHelper;
import com.common.utils.CommonApi;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class AuthenticateTest extends ReportHelper {
    CommonApi commonApi=new CommonApi();
    AuthenticateAdaptorImpl authenticateAdaptor;
    AuthenticateApiManager authenticateApiManager;

    @Parameters({ "environment" })
    @BeforeClass()
    public void initVariables(@Optional("staging") String environment)
    {
        Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
        authenticateAdaptor= new AuthenticateAdaptorImpl(envPropertyFile);
        authenticateApiManager = new AuthenticateApiManager();
    }

    @Test
    public void TestAuthenticationAPIStatus()
    {
        System.out.println(authenticateAdaptor.getAuthenticateServiceResponse().statusCode());
        System.out.println(authenticateAdaptor.getAuthToken());
    }
}