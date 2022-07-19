package com.airtel.RestAssured.Adtech.Tests.sms;

import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adconfigTest extends ReportHelper {

   

	CommonApi api = new CommonApi(); 
    @Test
    public void demo(){
        List<Header> header = new ArrayList<>();
       
        header.add(new Header("x-auth-token","Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1c2VyLXRhcmdldGluZy1hcGktcHJvZCIsInN1YiI6ImZWUm5VNUxsaExOdTNsTk5MMCIsImdhIjpbIlJFQURfQUNDRVNTIl0sImV4cCI6MTY1NTI3NTQwNSwiaWF0IjoxNjU1MTAyNjA1LCJqdGkiOiIyYmMxNzNjZi01MTI3LTRiNmMtYTIzZi1iZDFiZjg3NWUzYzAifQ.kNuViP4rTtWQXsQkMzP8pRsZaKaRTO68FDuv1PErw2cT4OmBJtUBhBhQKPoudrTf_8F7DYnZK2Io65_lPARceCzJ2CQ90o6zZQtm2OP2N6yh3sZhTAULUzXJrYbg_A1drdSANLox7dkGkHnqailvb9KylBAnUrjBcif7kiEx_LMFyjzSAmYivXfb5XIWxya15nw5TfhiG5Ol3UV4_pDB9wWzwmxLF-Fv9l9wUx5kr3gtyDxRdhwiOvV-Bh9vB2_SYo0vTzbUQIxhn7HJxy5xH6jnlYNp6ZEuofzz3xHUAYQ2lQf__F3bnqZoiizJmEgB2c1wohAppZvQZDpN_bafGg"));
        header.add(new Header("x-client-id","MUSIC_APP"));
        header.add(new Header("x-wynk-did","223f02b897ee0a22|Phone|Android|28|12605|1.22.0"));
        
//        Map<String,String> query = new HashMap<>();
//        query.put("id", "8806060281");
            
        Response res = api.getResponseWithHeaders(null,  header,"https://adconfig.wynk.in/wynk/ads/v3/adsConfig");
                      
        System.out.println("hello shubham");
        System.out.println(res.statusCode());
        System.out.println(res.asString());
         		
        
    }
    }
        
    