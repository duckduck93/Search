package com.example.search.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<GlobalErrorResponse> handleBadRequestException(HttpClientErrorException.BadRequest exception) {
        log.error(exception.getClass().getName(), exception.getMessage());
        GlobalErrorResponse response = new GlobalErrorResponse(exception.getStatusCode(), exception.getMessage());
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
