package com.Adtech.adaptor.Authenticate;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public interface AuthenticateAdaptor {
    Response getAuthenticateServiceResponse();
    String getAuthToken();

}
