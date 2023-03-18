package com.example.search.blog.exchange;

import com.example.search.blog.Blog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlogResponse {
    private final String title;
    private final String contents;
    private final String url;
    private final String blogName;
    private final String thumbnail;
    private final LocalDateTime createAt;

    public BlogResponse(Blog arg) {
        this.title = arg.getTitle();
        this.contents = arg.getContents();
        this.url = arg.getUrl();
        this.blogName = arg.getBlogName();
        this.thumbnail = arg.getThumbnail();
        this.createAt = arg.getCreateAt();
    }
}
