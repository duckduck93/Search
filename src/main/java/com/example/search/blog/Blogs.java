package com.example.search.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Blogs {
    private List<Blog> items;
    private Integer page;
    private Integer size;
    private Long total;
}
