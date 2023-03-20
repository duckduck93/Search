package com.example.search.blog.client;

public class ApiResponseSchemaErrorException extends RuntimeException {
    public ApiResponseSchemaErrorException(String clientName, Throwable cause) {
        super("%s Api의 Response가 지정한 Schema와 일치하지 않습니다.".formatted(clientName), cause);
    }
}
