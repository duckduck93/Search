package com.example.search.keyword;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordResponse {
    private final String name;
    private final Long count;

    public static KeywordResponse from(Keyword keyword) {
        return new KeywordResponse(keyword.getName(), keyword.getCount());
    }
}
