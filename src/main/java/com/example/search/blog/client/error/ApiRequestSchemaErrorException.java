package com.example.search.blog.client.error;

public class ApiRequestSchemaErrorException extends RuntimeException {
    public ApiRequestSchemaErrorException(String clientName, Throwable cause) {
        super("%s Api의 Request가 지정한 Schema와 일치하지 않습니다.".formatted(clientName), cause);
    }
}
