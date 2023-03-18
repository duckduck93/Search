package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.exchange.BlogSearchRequest;
import org.springframework.data.domain.Page;

public interface BlogSearchClient {
    Page<Blog> search(BlogSearchRequest request);
}
