package com.revolut.money.transfer.response;

import com.google.gson.annotations.Expose;
import com.revolut.money.transfer.enums.ErrorCodes;

public class GenericResponse<T> {
    @Expose
    private boolean success;
    @Expose
    private String description;
    @Expose
    private ErrorCodes errorCodes;
    @Expose
    T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
