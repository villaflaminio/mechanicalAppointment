package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.config.ApiError;
import it.mtempobono.mechanicalappointment.controller.RestExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandlerImpl implements RestExceptionHandler {
   private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
       return new ResponseEntity<Object>(apiError, apiError.getStatus());
   }

    @Override
    public ResponseEntity<Object> handleRunetimeException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
   }


}