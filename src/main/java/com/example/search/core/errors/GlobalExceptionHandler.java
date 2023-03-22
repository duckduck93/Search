package com.example.search.core.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GlobalErrorResponse> handleValidationBindingException(BindException exception) {
        log.error(exception.getClass().getName(), exception.getMessage());
        List<String> errors = exception.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage() == null ? "" : objectError.getDefaultMessage())
                .sorted()
                .toList();
        String message = String.join(", ", errors);

        GlobalErrorResponse error = new GlobalErrorResponse(HttpStatus.BAD_REQUEST, message, LocalDateTime.now());
        return new ResponseEntity<>(error, error.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<GlobalErrorResponse> handleHttpServerErrorException(HttpServerErrorException exception) {
        log.error(exception.getClass().getName(), exception.getMessage());
        GlobalErrorResponse error = new GlobalErrorResponse(exception.getStatusCode(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, error.getStatusCode());
    }
}
