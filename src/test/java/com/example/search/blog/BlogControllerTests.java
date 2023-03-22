package com.example.search.blog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
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
@AutoConfigureRestDocs
@DisplayName("Blog Controller Api 테스트")
@TestMethodOrder(MethodOrderer.MethodName.class)
class BlogControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("01. Api 조회 (All Parameter)")
    void _01_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "naver-line")
                        .queryParam("page", "3")
                        .queryParam("size", "40")
                        .queryParam("sort", "RECENCY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(3)))
                .andExpect(jsonPath("$.size", is(40)))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items", hasSize(40)));
    }

    @Test
    @DisplayName("02. Api 조회 (Required Parameter)")
    void _02_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "naver-financial")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items", hasSize(10)));
    }

    @Test
    @DisplayName("03. Api 조회 (Validation)")
    void _03_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("page", "0")
                        .queryParam("size", "51")
                        .queryParam("sort", "없는 값")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("ACCURACY, RECENCY 로 입력해주세요, page: 1 이상이여야 합니다, query: 검색어를 입력해주세요, size: 50 이하여야 합니다")));
    }

    @Test
    @DisplayName("04. Api 조회 (데이터 없는 케이스)")
    void _04_searchTest() throws Exception {
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("query", "먀ㅑㅠㅛㅗㅕindjgn야허ㅑdcvmkㅇ")
                        .queryParam("page", "3")
                        .queryParam("size", "40")
                        .queryParam("sort", "RECENCY")
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(3)))
                .andExpect(jsonPath("$.size", is(40)))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
