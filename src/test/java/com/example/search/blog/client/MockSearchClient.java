package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockSearchClient implements BlogSearchClient {
    private final int name;

    public MockSearchClient(int name) {
        this.name = name;
    }

    @Override
    public String getClientName() {
        return "%d".formatted(name);
    }

    @Override
    public boolean checkHealth() throws ApiServerErrorException {
        return true;
    }

    @Override
    public BlogSearchResult search(String keyword, SortType
            sort, int page) throws ApiServerErrorException, ApiResponseSchemaErrorException {
        return createMockResult(keyword, page);
    }

    private BlogSearchResult createMockResult(String keyword, int page) {
        List<Blog> items = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            items.add(new Blog("%d".formatted(name), "%02d %s".formatted(i, keyword), keyword, keyword, keyword, LocalDateTime.now()));
        }
        int offset = 50 * (page - 1);
        return new BlogSearchResult(items.subList(offset, offset + 50), 200L);
    }
}
