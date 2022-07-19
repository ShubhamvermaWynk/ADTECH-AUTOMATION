package com.airtel.common.dto.response;

import java.util.List;

public class AuthenticateResponseDTO {
	private String accessToken;
    //todo expireIn to be removed when iam v1 no longer used
    private Long expireIn;
    private Long expiresIn;
    private String userUuid;
    private String refreshToken;
    private String tokenType;

    //todo to be removed when iamv1 no longer used
    private List<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "AuthenticateResponseDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", expireIn=" + expireIn +
                ", expiresIn=" + expiresIn +
                ", userUuid='" + userUuid + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", roles=" + roles +
                '}';
    }

}
