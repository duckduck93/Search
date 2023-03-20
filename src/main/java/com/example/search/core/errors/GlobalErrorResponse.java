package com.example.search.core.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GlobalErrorResponse {
    private final HttpStatusCode statusCode;
    private final String message;
    private final LocalDateTime time;
}
