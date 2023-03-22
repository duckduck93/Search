package com.example.search.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    private String title;
    private String contents;
    private String url;
    private String blogName;
    private String thumbnail;
    private LocalDateTime createAt;

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", url='" + url + '\'' +
                ", blogName='" + blogName + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
