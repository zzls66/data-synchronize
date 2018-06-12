package com.shbaoyuantech.commons;

public class BizException extends RuntimeException {

    private String errorCode;
    private boolean propertiesKey = true;

    public BizException(String message) {
        super(message);
    }

    public BizException(String errorCode, String message) {
        this(errorCode, message, true);
    }

    public BizException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }

    public BizException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    public BizException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }
}
