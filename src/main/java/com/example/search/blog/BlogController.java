package com.example.search.blog;

import com.example.search.blog.exchange.BlogPageResponse;
import com.example.search.blog.exchange.BlogSearchRequest;
import lombok.RequiredArgsConstructor;
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
    public BlogPageResponse search(final BlogSearchRequest request) {
        Page<Blog> results = this.service.search(request);
        return new BlogPageResponse(results);
    }
}
