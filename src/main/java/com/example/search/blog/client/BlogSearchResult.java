package com.example.search.blog.client;

import com.example.search.blog.Blog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogSearchResult {
    private List<Blog> items;
    private Long total;
}
