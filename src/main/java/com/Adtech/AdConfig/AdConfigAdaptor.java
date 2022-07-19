package com.Adtech.AdConfig;

import com.airtel.teams.common.CommonApi;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.*;

public class AdConfigAdaptor extends CommonApi {

    String systemEnv;
    CommonApi commonApi = new CommonApi();

//    public AdConfigAdaptor(Properties envProperties){
//        systemEnv = envProperties.getProperty("");
//    }
//

   @Test
   public void demo(){
        List<Header> header = new ArrayList<>();
        header.add(new Header("subscribercode","Y2xtdGVjaDpwQDU1dzByZA=="));
        header.add(new Header("requestId","44e128a5-ac7a-4c9a-be4c-224b6bf81b20"));
        Map<String,String> query = new HashMap<>();
        query.put("id", "8806060281");

      Response res =  getResponseWithHeaders(query,header,"10.5.29.159:8300/adtechfederation/query/userDetails?id=8806060281");
      System.out.println(res.asString());
    }
}
