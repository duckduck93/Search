package com.example.search.blog;

import com.example.search.blog.client.error.AllApiServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayName("Blog Controller Api Mock 테스트")
@TestMethodOrder(MethodOrderer.MethodName.class)
class BlogControllerMockTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService service;

    private Blogs createMockResult(int page, int size) {
        List<Blog> items = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            items.add(new Blog("title", "Content %d".formatted(i), "https://naver.com", "블로그명", "https://naver.com", LocalDateTime.now()));
        }
        return new Blogs(items, page, size, 10L);
    }

    @Test
    @Description("01. Mock Data 반환 테스트")
    void _01_search() throws Exception {
        when(service.search(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(createMockResult(1, 10));

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "Query")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sort", "ACCURACY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andDo(document("blogs-search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.total", is(10)))
                .andExpect(jsonPath("$.items", hasSize(10)));
    }

    @Test
    @Description("02. Api 오류 시 Response 검증")
    void _02_search() throws Exception {
        when(service.search(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenThrow(new AllApiServerErrorException());

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "Query")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sort", "ACCURACY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andDo(document("blogs-search-error"))
                .andExpect(status().is5xxServerError());
    }
}
