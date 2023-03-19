package com.example.search.errors.blogs;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class AllApiServerErrorException extends HttpServerErrorException {
    public AllApiServerErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "모든 Api 서버가 오류 상태입니다.");
    }
}
