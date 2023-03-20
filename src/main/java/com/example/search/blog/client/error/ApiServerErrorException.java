package com.example.search.blog.client.error;

public class ApiServerErrorException extends RuntimeException {
    public ApiServerErrorException(String clientName) {
        super("%s Api Server Error로 조회되지 않습니다.".formatted(clientName));
    }
}
