package com.revolut.money.transfer.exception;

import com.revolut.money.transfer.enums.ErrorCodes;

public class CustomException extends  RuntimeException{
    ErrorCodes errorCode;

    public CustomException(ErrorCodes errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

}
