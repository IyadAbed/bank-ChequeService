package com.artSoft.bankChecks.handelException.exception;

import com.artSoft.bankChecks.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class CustomAuthenticationException extends ApiBaseException {


    public CustomAuthenticationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
