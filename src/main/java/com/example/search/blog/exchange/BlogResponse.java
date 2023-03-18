package com.example.search.blog.exchange;

import com.example.search.blog.Blog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogResponse {
    private final String title;
    private final String contents;
    private final String url;
    private final String blogName;
    private final String thumbnail;
    private final LocalDateTime createAt;

    public static BlogResponse from(Blog blog) {
        return new BlogResponse(
                blog.getTitle(),
                blog.getContents(),
                blog.getUrl(),
                blog.getBlogName(),
                blog.getThumbnail(),
                blog.getCreateAt()
        );
    }
}
