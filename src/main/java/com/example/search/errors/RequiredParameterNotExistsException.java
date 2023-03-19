package com.example.search.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class RequiredParameterNotExistsException extends HttpClientErrorException {
    public RequiredParameterNotExistsException() {
        super(HttpStatus.BAD_REQUEST, "검색어는 필수 값입니다.");
    }
}
