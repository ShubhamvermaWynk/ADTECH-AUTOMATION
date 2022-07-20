package com.Adtech.manager;

import com.common.utils.CommonApi;
import io.restassured.http.Header;
import io.restassured.response.Response;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AuthenticateApiManager{
    CommonApi commonApi= new CommonApi();
    Properties envProperty;
    Properties testData;

    public AuthenticateApiManager()
    {

        testData = commonApi.getTestDataConfigPropertyObject("authenticate");
    }


    public List<Header> getAuthenticateAPIHeader()
    {
        List<Header> header = new ArrayList<>();
        header.add(new Header("x-wynk-did",testData.getProperty("x-wynk-did").trim()));
        header.add(new Header("x-wynk-utkn",testData.getProperty("x-wynk-utkn").trim()));
       return header;
    }

    public String getBearerToken(Response response)
    {
        return response.getHeaders().get("x-auth-token").toString();
    }
}
