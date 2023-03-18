package com.example.search.blog.client.kakao.model;

import com.example.search.blog.Blog;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KakaoDocument {
    private String title;
    private String contents;
    private String url;
    @JsonProperty("blogname")
    private String blogName;
    private String thumbnail;
    @JsonProperty("datetime")
    private LocalDateTime createAt;

    public Blog toBlog() {
        return new Blog(
                this.title,
                this.contents,
                this.url,
                this.blogName,
                this.thumbnail,
                this.createAt
        );
    }
}
