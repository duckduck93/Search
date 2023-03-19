package com.example.search.blog.exchange;

import com.example.search.blog.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class BlogPageResponse {
    private Long total;
    private Integer size;
    private Integer page;
    private List<BlogResponse> items;

    public static BlogPageResponse from(Page<Blog> arg) {
        BlogPageResponse response = new BlogPageResponse();
        response.total = arg.getTotalElements();
        response.size = arg.getSize();
        response.page = arg.getNumber();
        response.items = arg.get().map(BlogResponse::from).toList();
        return response;
    }
}