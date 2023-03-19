package com.example.search.errors;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GlobalErrorResponse {
    private final HttpStatusCode statusCode;
    private final String message;

    public GlobalErrorResponse(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
