package com.example.search.blog.exchange;

import com.example.search.blog.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BlogResponse {
    private String title;
    private String contents;
    private String url;
    private String blogName;
    private String thumbnail;
    private LocalDateTime createAt;

    public static BlogResponse from(Blog blog) {
        BlogResponse response = new BlogResponse();
        response.title = blog.getTitle();
        response.contents = blog.getContents();
        response.url = blog.getUrl();
        response.blogName = blog.getBlogName();
        response.thumbnail = blog.getThumbnail();
        response.createAt = blog.getCreateAt();
        return response;
    }
}
