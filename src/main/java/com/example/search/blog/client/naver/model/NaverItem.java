package com.example.search.blog.client.naver.model;

import com.example.search.blog.Blog;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NaverItem {
    private String title;
    private String link;
    private String description;
    @JsonProperty("bloggername")
    private String bloggerName;
    @JsonProperty("bloggerlink")
    private String bloggerLink;
    @JsonProperty("postdate")
    private LocalDate postDate;

    public Blog toBlog() {
        return new Blog(
                this.title,
                this.description,
                this.link,
                this.bloggerName,
                this.bloggerLink,
                this.postDate.atStartOfDay()
        );
    }
}
