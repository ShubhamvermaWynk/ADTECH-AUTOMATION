package com.Adtech.Authentication.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.Test;

import com.Adtech.Authorization.AuthorizationImpl;
import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;

import io.restassured.http.Header;
import io.restassured.response.Response;



public class AuthenticationTest extends ReportHelper {
	CommonApi commonApi = new CommonApi();
	
	
	AuthorizationImpl authorizationImpl;
	
	@Test
	public void TestAuthenticationAPI()
	{
		 authorizationImpl = new AuthorizationImpl();
		 List<Header> header = new ArrayList<>();
		 header.add(new Header("x-wynk-did","223f02b897ee0a22|Phone|Android|28|12605|1.22.0"));
	     header.add(new Header("x-wynk-utkn","fVRnU5LlhLNu3lNNL0:lCp6NPX5H2T1zGnYDxuR4j+3U4s="));   
		 Map<String,String> query = new HashMap<>();
         String token = authorizationImpl.getAuthorizationToken(null, header,null,"https://adconfig.wynk.in/api/v1/authenticate", false);
		 System.out.println(token);
		
		 
	     Properties  propertyFile = commonApi.getTestDataConfigPropertyObject("AuthorizationApi");
	     propertyFile.setProperty(token, token);
	     propertyFile.get("x-auth-token");
	     System.out.println( "shubham   "+  propertyFile.get("x-auth-token"));
	        
	        
	}

}
