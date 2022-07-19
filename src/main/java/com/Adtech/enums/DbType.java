package com.Adtech.enums;

public enum DbType {

    MYSQL,ORACLE;

    public String getErrorCode() {
        return toString();
    }
}

