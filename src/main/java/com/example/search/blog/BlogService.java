package com.example.search.blog;

import com.example.search.blog.client.kakao.KakaoSearchClient;
import com.example.search.blog.client.naver.NaverSearchClient;
import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.errors.blogs.AllApiServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {
    private final RabbitTemplate rabbitTemplate;

    private final KakaoSearchClient kakaoClient;
    private final NaverSearchClient naverClient;

    public Page<Blog> search(final BlogSearchRequest request) {
        this.rabbitTemplate.convertAndSend("search.exchange", "search.routing.#", request.getQuery());

        try {
            return kakaoClient.search(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            return naverClient.search(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        throw new AllApiServerErrorException();
    }
}
