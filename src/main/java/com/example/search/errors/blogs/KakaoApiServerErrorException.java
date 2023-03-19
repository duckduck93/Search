package com.example.search.errors.blogs;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class KakaoApiServerErrorException extends HttpServerErrorException {
    public KakaoApiServerErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Kakao Api 서버가 오류가 상태입니다.");
    }
}
