package com.example.search.keyword;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Keyword {
    private final String name;
    private Long count;

    public static Keyword from(KeywordEntity entity) {
        return new Keyword(entity.getName(), entity.getCount());
    }

    public void increaseCount() {
        this.count += 1;
    }
}
