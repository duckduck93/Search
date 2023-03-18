package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogSearchClient client;
    private final KeywordService keywordService;

    public Page<Blog> search(final BlogSearchRequest request) {
        this.keywordService.increaseCount(request.getQuery());
        return client.search(request);
    }
}
