package com.example.search.blog.client;

import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;


public interface BlogSearchClient {
    String getClientName();

    boolean checkHealth() throws ApiServerErrorException;

    BlogSearchResult search(String keyword, SortType sort, int page) throws ApiServerErrorException, ApiResponseSchemaErrorException;
}
