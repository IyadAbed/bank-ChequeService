package com.artSoft.bankChecks.handelException.exception;

import com.artSoft.bankChecks.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApiBaseException {
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
