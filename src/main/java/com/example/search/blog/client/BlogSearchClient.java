package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.util.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogSearchClient {
    Page<Blog> search(String keyword, SortType sort, Pageable pageable);
}
