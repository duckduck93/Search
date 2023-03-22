package com.example.search.keyword.message;

import com.example.search.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor

public class DefaultKeywordCountPublisher implements KeywordCountPublisher {
    private final KeywordService service;

    @Override
    public void increase(String keyword) {
        service.increaseCount(keyword);
    }
}
