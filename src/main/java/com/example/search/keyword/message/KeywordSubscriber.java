package com.example.search.keyword.message;

import com.example.search.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordSubscriber {
    private final KeywordService service;

    @RabbitListener(queues = "search.queue")
    public void increaseCount(String keywordName) {
        log.info("Rabbitmq keywordName = " + keywordName);
        this.service.increaseCount(keywordName);
    }
}
