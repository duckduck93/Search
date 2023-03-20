package com.example.search.blog;

import com.example.search.blog.exchange.BlogPageResponse;
import com.example.search.blog.exchange.BlogSearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping
    public BlogPageResponse search(@Valid final BlogSearchRequest request) {
        Page<Blog> results = this.service.search(request.getQuery(), request.getSortType(), request.getPage(), request.getSize());
        return BlogPageResponse.from(results);
    }

    @GetMapping(path = "/cache")
    @Cacheable(cacheManager = "RedisCacheManager", value = "blogs", key = "#request.query + '|' + #request.page + '|' + #request.size + '|' + #request.sortType")
    public BlogPageResponse searchCache(final BlogSearchRequest request) {
        return this.search(request);
    }
}
