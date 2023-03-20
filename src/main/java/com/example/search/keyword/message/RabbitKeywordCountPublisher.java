package com.example.search.keyword.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitKeywordCountPublisher implements KeywordCountPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void increase(String keyword) {
        this.rabbitTemplate.convertAndSend("search.exchange", "search.routing.#", keyword);
    }
}
