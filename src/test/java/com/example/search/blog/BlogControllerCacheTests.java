package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.util.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
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
    CacheManager cacheManager;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BlogSearchClient client;

    @Test
    @DisplayName("01. Caching 검증")
    void _01_searchTest() throws Exception {
        BlogSearchRequest before = new BlogSearchRequest("before", 0, 1, SortType.ACCURACY);
        given(client.search(ArgumentMatchers.any())).willReturn(createTemporaryData(before));

        ResultActions result1 = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "title1")
        );
        result1.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(before.getPage())))
                .andExpect(jsonPath("$.size", is(before.getSize())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.items[0].title", is(before.getQuery())));

        BlogSearchRequest after = new BlogSearchRequest("after", 0, 1, SortType.ACCURACY);
        given(client.search(ArgumentMatchers.any())).willReturn(createTemporaryData(after));

        ResultActions result2 = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "title1")
        );
        result2.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(before.getPage())))
                .andExpect(jsonPath("$.size", is(before.getSize())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.items[0].title", is(before.getQuery()))); // before 일치한다면 caching 성공
    }

    private Page<Blog> createTemporaryData(BlogSearchRequest request) {
        List<Blog> items = new ArrayList<>();
        items.add(new Blog(
                request.getQuery(),
                request.getQuery(),
                request.getQuery(),
                request.getQuery(),
                request.getQuery(),
                LocalDateTime.now()
        ));
        return new PageImpl<>(items, request, 1);
    }
}
