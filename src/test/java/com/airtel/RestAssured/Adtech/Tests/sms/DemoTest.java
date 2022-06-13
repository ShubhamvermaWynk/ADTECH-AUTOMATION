package com.airtel.RestAssured.Adtech.Tests.sms;

import com.airtel.teams.common.CommonApi;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class DemoTest extends CommonApi {

    @Test
    public void demo()
    {
     //   Response response = fetchApiResponse("https://adconfig.wynk.in/wynk/ads/v3/adsConfig", null,null, false, false, null, null, CommonApi.uniqueidentifier());
        Response response1 = fetchApiResponse("https://adconfig.wynk.in/wynk/ads/v3/adsConfig","GET","",null
        ,null, ContentType.JSON,true,null);
     //   (String requestUrl, String requestType, String body, Map<String, String> queryParams, List<Header> headers, ContentType contentType, boolean checkStatus, Map<String, String> formParams
        System.out.println(response1.statusCode());
    }
}
