package com.example.search.blog;

import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.blog.exchange.BlogsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping
    public BlogsResponse search(@Valid final BlogSearchRequest request) {
        Blogs results = this.service.search(request.getQuery(), request.getSort(), request.getPage(), request.getSize());
        return BlogsResponse.from(results);
    }
}
