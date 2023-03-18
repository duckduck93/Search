package com.example.search.blog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Blog Controller Api 테스트")
class BlogControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("01. Kakao Api 조회 (All Parameter)")
    void _01_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "카카오뱅크")
                        .queryParam("page", "2")
                        .queryParam("size", "5")
                        .queryParam("sort", "ACCURACY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(2)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items", hasSize(5)));
    }

    @Test
    @DisplayName("02. Kakao Api 조회 (Required Paramter)")
    void _02_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "카카오뱅크")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items", hasSize(10)));
    }

    @Test
    @DisplayName("03. Kakao Api 조회 (query 미포함)")
    void _03_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("page", "2")
                        .queryParam("size", "5")
                        .queryParam("sort", "ACCURACY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }
}
