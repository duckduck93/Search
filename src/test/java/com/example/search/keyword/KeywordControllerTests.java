package com.example.search.keyword;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@DisplayName("Keyword Controller Api 테스트")
@TestMethodOrder(MethodOrderer.MethodName.class)
class KeywordControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("01. Blog Api 조회하여 Keyword 쌓기")
    void _01_keywordCreateTest() throws Exception {
        String[] keywords = {
                "카카오", "카카오게임즈", "카카오모빌리티", "카카오페이", "카카오엔터프라이즈",
                "카카오i", "카카오M", "카카오벤처스", "카카오X", "카카오뱅크",
                "하하하",

                "카카오뱅크", "카카오뱅크", "카카오뱅크", "카카오뱅크",
                "카카오", "카카오", "카카오",
        };
        for (String keyword : keywords) {
            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/blogs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .queryParam("query", keyword)
            );
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.page", is(1)))
                    .andExpect(jsonPath("$.size", is(10)))
                    .andExpect(jsonPath("$.total").exists())
                    .andExpect(jsonPath("$.items", hasSize(10)));
        }
    }

    @Test
    @DisplayName("02. Keyword Api 인기검색어 조회하기")
    void _02_findPopularKeyword() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/keywords/popular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].name", is("카카오뱅크")))
                .andExpect(jsonPath("$[0].count", is(5)))
                .andExpect(jsonPath("$[1].name", is("카카오")))
                .andExpect(jsonPath("$[1].count", is(4)))
                .andExpect(jsonPath("$[2].name", is("카카오i")))
                .andExpect(jsonPath("$[2].count", is(1)))
                .andExpect(jsonPath("$[3].name", is("카카오M")))
                .andExpect(jsonPath("$[4].name", is("카카오X")))
                .andExpect(jsonPath("$[5].name", is("카카오게임즈")))
                .andExpect(jsonPath("$[6].name", is("카카오모빌리티")))
                .andExpect(jsonPath("$[7].name", is("카카오벤처스")))
                .andExpect(jsonPath("$[8].name", is("카카오엔터프라이즈")))
                .andExpect(jsonPath("$[9].name", is("카카오페이")));
    }


    @Test
    @DisplayName("03. Keyword Api 인기검색어 조회하기 (maxCount 3)")
    void _03_findPopularKeyword() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/keywords/popular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("size", "3")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("카카오뱅크")))
                .andExpect(jsonPath("$[0].count", is(5)))
                .andExpect(jsonPath("$[1].name", is("카카오")))
                .andExpect(jsonPath("$[1].count", is(4)))
                .andExpect(jsonPath("$[2].name", is("카카오i")))
                .andExpect(jsonPath("$[2].count", is(1)));
    }

    @Test
    @DisplayName("03. Keyword Api 인기검색어 조회하기 (maxCount > 10 예외 발생)")
    void _04_findPopularKeyword() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/keywords/popular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("size", "11")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("size: 10 이하여야 합니다")));
    }
}
