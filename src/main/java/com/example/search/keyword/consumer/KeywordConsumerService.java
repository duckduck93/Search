package com.example.search.keyword.consumer;

import com.example.search.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordConsumerService {
    private final KeywordService service;

    @RabbitListener(queues = "search.queue")
    public void increaseCount(String keywordName) {
        log.info("Rabbitmq keywordName = " + keywordName);
        this.service.increaseCount(keywordName);
    }
}
