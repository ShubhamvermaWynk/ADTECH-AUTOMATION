package com.adtech.restAssured.tests.personalisation;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Adtech.adaptor.personalization.PersonlaizationAdaptor;
import com.Adtech.adaptor.personalization.PersonlaizationAdaptorImpl;
import com.airtel.helper.report.ReportHelper;
import com.common.utils.CommonApi;

import io.restassured.response.Response;

public class PersonalisationTest extends ReportHelper {
	
	CommonApi commonApi=new CommonApi();
	
	PersonlaizationAdaptor perObj;

	@Parameters({ "environment" })
	@BeforeClass()
	public void initVariables(@Optional("staging") String environment)
	{
		Properties envPropertyFile = commonApi.getConfigPropertyObject(environment);
		perObj= new PersonlaizationAdaptorImpl(envPropertyFile);

		
	}
	
	@Test(description="Msisdn without B prefix")
	public void verifyIncorrectMsisdn()
	{
		Response response= perObj.getOrderList("123353535345");
		if(response.getStatusCode()!=400)
		{
			ReportHelper.logValidationFailure("Status Code", "400", String.valueOf(response.getStatusCode()), "Incorrect status code");
			Assert.assertTrue(false);
		}
	}
	

}
