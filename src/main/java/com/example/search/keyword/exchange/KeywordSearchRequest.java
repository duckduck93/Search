package com.example.search.keyword.exchange;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class KeywordSearchRequest {
    @Min(value = 1, message = "size: 1 이상이여야 합니다")
    @Max(value = 10, message = "size: 10 이하여야 합니다")
    private final Integer size;

    public Integer getSize() {
        return Optional.ofNullable(size).orElse(10);
    }
}
