package com.example.search.blog.client.naver.model;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverResponse {
    private String lastBuildDate;
    private long total;
    private long start;
    private long display;
    private List<NaverItem> items;
}
