package com.artSoft.bankChecks.handelException.exception;

import com.artSoft.bankChecks.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class BadReqException extends ApiBaseException {
   public BadReqException(String message) {
      super(message);
   }

   @Override
   public HttpStatus getStatusCode() {
      return HttpStatus.BAD_REQUEST;
   }
}
