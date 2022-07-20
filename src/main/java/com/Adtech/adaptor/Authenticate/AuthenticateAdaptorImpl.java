package com.Adtech.adaptor.Authenticate;

import com.Adtech.constants.AdtechApiConstants;
import com.Adtech.manager.AuthenticateApiManager;
import com.common.utils.CommonApi;
import io.restassured.response.Response;

import java.util.Properties;

public class AuthenticateAdaptorImpl  extends CommonApi implements AuthenticateAdaptor , AdtechApiConstants {
    String serverInitials;
    AuthenticateApiManager authenticateApiManager = new AuthenticateApiManager();

    public AuthenticateAdaptorImpl(Properties envProperty) {
        serverInitials = envProperty.getProperty("AUTHENTICATE_URL");
    }
    @Override
    public Response getAuthenticateServiceResponse() {

        return getPostResponse("", serverInitials,POST_AUTHENTICATE_URL , false, authenticateApiManager.getAuthenticateAPIHeader());
    }

    @Override
    public String getAuthToken() {
    return  authenticateApiManager.getBearerToken(getAuthenticateServiceResponse());
    }
}
