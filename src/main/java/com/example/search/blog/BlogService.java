package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClients;
import com.example.search.blog.client.error.AllApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import com.example.search.keyword.message.KeywordCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {
    private final KeywordCountService keywordCountService;
    private final BlogSearchClients clients;

    public Blogs search(String keyword, SortType sort, int page, int size) throws AllApiServerErrorException {
        log.info("BlogService: %s %s %d %d".formatted(keyword, sort, page, size));
        keywordCountService.increase(keyword);
        return clients.search(keyword, sort, page, size);
    }
}
