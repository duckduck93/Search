package com.example.search.blog.exchange;

import com.example.search.blog.Blog;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BlogPageResponse {
    private final Long total;
    private final Integer size;
    private final Integer page;
    private final List<BlogResponse> items;

    public BlogPageResponse(Page<Blog> arg) {
        this.total = arg.getTotalElements();
        this.size = arg.getSize();
        this.page = arg.getNumber();
        this.items = arg.get().map(BlogResponse::new).toList();
    }
}