package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.client.naver.NaverSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.blog.exchange.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class NaverSearchClientTests {
    private final NaverSearchClient client = new NaverSearchClient(
            "https://openapi.naver.com/v1/search/blog.json",
            "-",
            "-"
    );

    @Test
    void _01_search() {
        Page<Blog> result = client.search(new BlogSearchRequest("naver", 1, 10, SortType.ACCURACY));
        Assertions.assertThat(result.getNumber()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(10);
        Assertions.assertThat(result.get()).hasSize(10);
    }
}
