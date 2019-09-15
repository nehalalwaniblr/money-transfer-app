package com.revolut.money.transfer.util;

import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.response.GenericResponse;

public class ResponseUtil {
    public static GenericResponse createExceptionResponse(String description, ErrorCodes errorCodes) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setSuccess(false);
        genericResponse.setDescription(description);
        genericResponse.setErrorCodes(errorCodes);

        return genericResponse;
    }
}
