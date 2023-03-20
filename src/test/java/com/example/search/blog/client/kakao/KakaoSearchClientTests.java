package com.example.search.blog.client.kakao;

import com.example.search.blog.Blog;
import com.example.search.blog.client.error.ApiRequestSchemaErrorException;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

class KakaoSearchClientTests {
    @Test
    @DisplayName("01. Kakao Api 조회")
    void _01_search() {
        KakaoSearchClient client = new KakaoSearchClient(
                "https://dapi.kakao.com/v2/search/blog",
                "x"
        );
        Page<Blog> result = client.search("Kakao", SortType.ACCURACY, 1, 5);
        Assertions.assertThat(result.getNumber()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(5);
        Assertions.assertThat(result.get()).hasSize(5);
    }

    @Test
    @DisplayName("02. Kakao Api 조회 (400 Error)")
    void _02_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/400", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1, 5))
                .isInstanceOf(ApiRequestSchemaErrorException.class);
    }

    @Test
    @DisplayName("03. Kakao Api 조회 (500 Error)")
    void _03_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/500", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1, 5))
                .isInstanceOf(ApiServerErrorException.class);
    }

    @Test
    @DisplayName("04. Kakao Api 조회 (200 Schema 변경)")
    void _04_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/200", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1, 5))
                .isInstanceOf(ApiResponseSchemaErrorException.class);
    }
}
