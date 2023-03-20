package com.example.search.blog;

import com.example.search.blog.client.kakao.KakaoSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.blog.exchange.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Blog Controller Api Caching 테스트")
@TestMethodOrder(MethodOrderer.MethodName.class)
class BlogControllerCacheTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private KakaoSearchClient client;

    @Test
    @DisplayName("01. Caching 검증")
    void _01_searchCacheTest() throws Exception {
        // 캐싱 적용
        String before = "query1";
        given(client.search(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(createTemporaryData(before));
        searchCacheRequestAndCheckResponse(before, before);
        // 캐싱 사용
        String after = "query1-after";
        given(client.search(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(createTemporaryData(after));
        searchCacheRequestAndCheckResponse(before, before);
    }

    @Test
    @DisplayName("02. TTL 적용 검증")
    void _02_searchCacheTest() throws Exception {
        // 캐싱 적용
        String before = "query2";
        given(client.search(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(createTemporaryData(before));
        searchCacheRequestAndCheckResponse(before, before);
        // 캐싱 만료
        Thread.sleep(10 * 1000);
        // 캐싱 적용
        String after = "query2-after";
        given(client.search(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(createTemporaryData(after));
        searchCacheRequestAndCheckResponse(after, after);
    }

//    @Test
//    @DisplayName("03. Caching Keyword Count effect")
//    void _03_searchCacheTest() throws Exception {
//        // 캐싱 적용
//        String before = "query3";
//        given(client.search(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(createTemporaryData(before));
//        searchCacheRequestAndCheckResponse(before, before);
//        searchCacheRequestAndCheckResponse(before, before);
//
//        ResultActions result = mockMvc.perform(
//                MockMvcRequestBuilders.get("/keywords/popular")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//        result.andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name", is("query3")))
//                .andExpect(jsonPath("$[0].count", is("2")));
//    }

    private void searchCacheRequestAndCheckResponse(String query, String expect) throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs/cache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", query)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(0)))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.items[0].title", is(expect)));
    }

    private Page<Blog> createTemporaryData(String response) {
        List<Blog> items = new ArrayList<>();
        items.add(new Blog(
                response,
                response,
                response,
                response,
                response,
                LocalDateTime.now()
        ));
        return new PageImpl<>(items, new BlogSearchRequest(response, 0, 0, SortType.ACCURACY), 1);
    }
}
