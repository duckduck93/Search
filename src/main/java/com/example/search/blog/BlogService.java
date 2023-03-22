package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClients;
import com.example.search.blog.exchange.SortType;
import com.example.search.keyword.message.KeywordCountPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {
    private final KeywordCountPublisher keywordCountPublisher;
    private final BlogSearchClients clients;

    public Blogs search(String keyword, SortType sort, int page, int size) {
        log.info("BlogService: %s %s %d %d".formatted(keyword, sort, page, size));
        keywordCountPublisher.increase(keyword);
        return clients.search(keyword, sort, page, size);
    }
}
