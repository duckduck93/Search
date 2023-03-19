package com.example.search.blog.client.kakao.model;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoResponse {
    private KakaoMeta meta;
    private List<KakaoDocument> documents;
}

