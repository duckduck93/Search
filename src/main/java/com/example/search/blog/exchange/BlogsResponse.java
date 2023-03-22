package com.example.search.blog.exchange;

import com.example.search.blog.Blogs;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BlogsResponse {
    private Long total;
    private Integer size;
    private Integer page;
    private List<BlogResponse> items;

    public static BlogsResponse from(Blogs arg) {
        BlogsResponse response = new BlogsResponse();
        response.total = arg.getTotal();
        response.size = arg.getSize();
        response.page = arg.getPage();
        response.items = arg.getItems().stream().map(BlogResponse::from).toList();
        return response;
    }
}