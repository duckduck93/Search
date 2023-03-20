package com.example.search.blog.exchange;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BlogSearchRequest {
    @NotBlank(message = "query: 검색어를 입력해주세요")
    private final String query;

    private final SortType sortType;

    @Min(value = 1, message = "page: 1 이상이여야 합니다")
    @Max(value = 50, message = "page: 50 이하여야 합니다")
    private final Integer page;

    @Min(value = 1, message = "size: 1 이상이여야 합니다")
    @Max(value = 50, message = "size: 50 이하여야 합니다")
    private final Integer size;

    public String getQuery() {
        return query;
    }

    public SortType getSortType() {
        return Optional.ofNullable(sortType).orElse(SortType.ACCURACY);
    }

    public int getPage() {
        return Optional.ofNullable(page).orElse(1);
    }

    public int getSize() {
        return Optional.ofNullable(size).orElse(10);
    }
}
