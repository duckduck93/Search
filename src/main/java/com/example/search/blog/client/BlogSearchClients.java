package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.exchange.SortType;
import com.example.search.errors.blogs.AllApiServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogSearchClients {
    private final List<BlogSearchClient> clients;

    /**
     * BlogSearchClient 구현체를 Loop 돌면서 블로그 Api 조회
     *
     * @param keyword 검색 keyword
     * @param sort    정렬방식
     * @param page    페이지
     * @param size    조회건수
     * @return 블로그 Api 조회 결과
     */
    public Page<Blog> search(String keyword, SortType sort, int page, int size) {
        for (BlogSearchClient client : clients) {
            try {
                return client.search(keyword, sort, page, size);
            } catch (ApiServerErrorException | ApiResponseSchemaErrorException e) {
                log.error(e.getMessage(), e.getCause());
            }
        }
        throw new AllApiServerErrorException();
    }
}
