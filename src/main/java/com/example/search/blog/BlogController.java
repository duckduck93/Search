package com.example.search.blog;

import com.example.search.blog.exchange.BlogPageResponse;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.util.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping
    public BlogPageResponse search(final BlogSearchRequest request, final Pageable pageable) {
        if (request.getSort() == null) request.setSort(SortType.ACCURACY);

        Page<Blog> results = this.service.search(request, pageable);
        return new BlogPageResponse(results);
    }
}
