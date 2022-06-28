package com.pinakibarik.coinexchangeapi.exception;

import org.apache.commons.lang3.StringUtils;

public class ApplicationException extends RuntimeException {
    private String errorCode = StringUtils.EMPTY;

    public String getErrorCode() {
        return errorCode;
    }

    private ApplicationException() {
    }

    public ApplicationException(Exception ex) {
        super(ex);
    }

    public ApplicationException(String key) {
        super();
        this.errorCode = key;
    }

    public ApplicationException(String key, Exception ex) {
        super(ex);
        this.errorCode = key;
    }
}
