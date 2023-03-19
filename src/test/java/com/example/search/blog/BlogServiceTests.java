package com.example.search.blog;

import com.example.search.blog.client.kakao.KakaoSearchClient;
import com.example.search.blog.client.naver.NaverSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.errors.blogs.AllApiServerErrorException;
import com.example.search.errors.blogs.KakaoApiServerErrorException;
import com.example.search.errors.blogs.NaverApiServerErrorException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BlogServiceTests {
    @InjectMocks
    private BlogService service;

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private KakaoSearchClient kakaoClient;
    @Mock
    private NaverSearchClient naverClient;

    @Test
    @Description("01. Kakao Api 장애 시 Naver Api 호출 테스트")
    void _01_search() {
        given(kakaoClient.search(ArgumentMatchers.any())).willThrow(new KakaoApiServerErrorException());
        given(naverClient.search(ArgumentMatchers.any())).willReturn(createMockResult());

        BlogSearchRequest request = new BlogSearchRequest("keyword");
        Page<Blog> result = this.service.search(request);

        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getSize()).isEqualTo(1);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1L);
        Assertions.assertThat(result.get().toList()).hasSize(1);
    }

    @Test
    @Description("02. Kakao Api, Naver Api 전체 장애")
    void _02_search() {
        given(kakaoClient.search(ArgumentMatchers.any())).willThrow(new KakaoApiServerErrorException());
        given(naverClient.search(ArgumentMatchers.any())).willThrow(new NaverApiServerErrorException());

        BlogSearchRequest request = new BlogSearchRequest("keyword");
        Assertions.assertThatThrownBy(() -> this.service.search(request)).isInstanceOf(AllApiServerErrorException.class);
    }

    private Page<Blog> createMockResult() {
        List<Blog> items = new ArrayList<>();
        items.add(new Blog("keyword", "keyword", "keyword", "keyword", "keyword", LocalDateTime.now()));
        return new PageImpl<>(items);
    }
}
