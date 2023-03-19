package com.example.search.errors.blogs;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class NaverApiServerErrorException extends HttpServerErrorException {
    public NaverApiServerErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Naver Api 서버가 오류가 상태입니다.");
    }
}

