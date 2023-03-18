package com.example.search.blog.client.kakao.model;

import com.example.search.blog.Blog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KakaoDocument {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private LocalDateTime datetime;

    public Blog toBlog() {
        return new Blog(
                this.title,
                this.contents,
                this.url,
                this.blogname,
                this.thumbnail,
                this.datetime
        );
    }
}
