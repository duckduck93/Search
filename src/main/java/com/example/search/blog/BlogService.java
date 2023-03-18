package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogSearchClient client;

    public Page<Blog> search(final BlogSearchRequest request, final Pageable pageable) {
        return client.search(request.getQuery(), request.getSort(), pageable);
    }
}
