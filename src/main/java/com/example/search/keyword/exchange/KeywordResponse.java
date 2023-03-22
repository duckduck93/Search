package com.example.search.keyword.exchange;

import com.example.search.keyword.Keyword;
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
