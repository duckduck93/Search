package com.example.search.blog.exchange;

import com.example.search.util.SortType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogSearchRequest {
    @NotEmpty
    private String query;

    private SortType sort;
}
