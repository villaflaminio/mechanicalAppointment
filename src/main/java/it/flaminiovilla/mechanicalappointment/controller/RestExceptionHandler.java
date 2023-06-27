package it.flaminiovilla.mechanicalappointment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleRunetimeException(RuntimeException ex);

}
