package com.example.search.blog.client.kakao;

import com.example.search.blog.client.BlogSearchResult;
import com.example.search.blog.client.error.ApiRequestSchemaErrorException;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KakaoSearchClientTests {
    @Test
    @DisplayName("01. Kakao Api 조회")
    void _01_search() {
        KakaoSearchClient client = new KakaoSearchClient(
                "https://dapi.kakao.com/v2/search/blog",
                "x"
        );
        BlogSearchResult result = client.search("Kakao", SortType.ACCURACY, 1);
        Assertions.assertThat(result.getItems()).isNotNull();
        Assertions.assertThat(result.getTotal()).isNotNull();
    }

    @Test
    @DisplayName("02. Kakao Api 조회 (400 Error)")
    void _02_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/400", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1))
                .isInstanceOf(ApiRequestSchemaErrorException.class);
    }

    @Test
    @DisplayName("03. Kakao Api 조회 (500 Error)")
    void _03_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/500", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1))
                .isInstanceOf(ApiServerErrorException.class);
    }

    @Test
    @DisplayName("04. Kakao Api 조회 (200 Schema 변경)")
    void _04_search() {
        KakaoSearchClient client = new KakaoSearchClient("https://mock.codes/200", "");
        Assertions
                .assertThatThrownBy(() -> client.search("", SortType.ACCURACY, 1))
                .isInstanceOf(ApiResponseSchemaErrorException.class);
    }
}
