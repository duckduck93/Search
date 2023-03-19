package com.example.search.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class RequestParameterMaxValueException extends HttpClientErrorException {

    public RequestParameterMaxValueException(int maxCount) {
        super(HttpStatus.BAD_REQUEST, "검색 요청은 %d 미만으로 지정해주세요.".formatted(maxCount));
    }
}
