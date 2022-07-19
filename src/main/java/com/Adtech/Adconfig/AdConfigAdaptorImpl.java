package com.Adtech.Adconfig;

import com.airtel.teams.common.CommonApi;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.*;

public class AdConfigAdaptorImpl extends CommonApi {
	
  //  String systemEnv;
    CommonApi commonApi = new CommonApi();

//    public AdConfigAdaptor(Properties envProperties){
//        systemEnv = envProperties.getProperty("");
//    }
//

   @Test
   public void demoo(){
        List<Header> header = new ArrayList<>();
        header.add(new Header("x-auth-token","Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1c2VyLXRhcmdldGluZy1hcGktcHJvZCIsInN1YiI6ImZWUm5VNUxsaExOdTNsTk5MMCIsImdhIjpbIlJFQURfQUNDRVNTIl0sImV4cCI6MTY1Nzk1Njc1OCwiaWF0IjoxNjU3NzgzOTU4LCJqdGkiOiI4OGZlYjM0Ni00YWVhLTQyODYtYThhYS05MzBlNDEyYjE3M2QifQ.Y1o2IAw02uBaBRzvtYYCKvlzoGWE90dRboMPIbEo0a7AN4YUMR72Ui_zEZLwC4hv3oYAKXb6SNmALxXiKzvtyIJsGt84TFB2PJN-4qexVNxysUPJR2PxLdRs4WuzVjKHdEh1hslRs2u5R7gu758U6B8xlh9pm1YTxp3gBpzDgx8dygFblXTNkovzcxFiznLj0tf2F0ICVAZGEjmC0UfHgKFcXHX5WDCdtuGtTSosHSSuKV98flyjW1aM-v55Gi7WcejYlrCuTBax-nWfWv-H-X2cRXoNoXZ2RrEFL2mVRPon8KG0vc_b6CucI44gACloVt47kcMJrMtVJIc98MwyuA"));
        header.add(new Header("x-client-id","AIRTEL_THANKS"));
        header.add(new Header("x-wynk-did","53d7b77257fd991c|Phone|Android|28|12605|1.22.0"));
        header.add(new Header("x-bsy-utkn","hDN1MYuq6nS5JsvAd0:cILQkGI02VrmzsyIex0KB2ST2LI="));
        header.add(new Header("x-bsy-did","53d7b77257fd991c=="));
        header.add(new Header("x-bsy-dt","El6I6VXg"));
        header.add(new Header("x-bsy-advId","test1"));
        Map<String,String> query = new HashMap<>();
    

      Response res =  getResponseWithHeaders(query ,header,"https://adconfig.wynk.in/wynk/ads/v3/adsConfig",false);

      System.out.println(res.statusCode());
      System.out.println("hello " +res.asString());
      
      }
 

}
