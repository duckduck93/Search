package com.example.search.blog.client.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KakaoApiInfo {
    @Value("${kakao.url}")
    private String url;
    @Value("${kakao.key}")
    private String key;
}
