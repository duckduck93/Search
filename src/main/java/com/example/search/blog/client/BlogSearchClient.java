package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import org.springframework.data.domain.Page;


public interface BlogSearchClient {
    Page<Blog> search(String keyword, SortType sort, int page, int size) throws ApiServerErrorException, ApiResponseSchemaErrorException;
}
