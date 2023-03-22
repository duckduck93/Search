package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.BlogSearchResult;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;

public class ErrorSearchClient implements BlogSearchClient {
    private final int name;

    public ErrorSearchClient(int name) {
        this.name = name;
    }

    @Override
    public String getClientName() {
        return "%d".formatted(name);
    }

    @Override
    public boolean checkHealth() throws ApiServerErrorException {
        return false;
    }

    @Override
    public BlogSearchResult search(String keyword, SortType
            sort, int page) throws ApiServerErrorException, ApiResponseSchemaErrorException {
        throw new ApiServerErrorException("%d".formatted(name));
    }
}
