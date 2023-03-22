package com.example.search.blog.client;

import com.example.search.blog.Blog;
import com.example.search.blog.Blogs;
import com.example.search.blog.client.error.AllApiServerErrorException;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public Blogs search(String keyword, SortType sort, int page, int size) throws AllApiServerErrorException {
        for (BlogSearchClient client : clients) {
            try {
                if (!client.checkHealth()) throw new ApiServerErrorException(client.getClientName());

                int end = page * size;
                int sta = end - size + 1;

                int staPage = (sta / 50) + 1;
                int endPage = (end / 50) + 1;
                List<Blog> allItems = new ArrayList<>(100);
                long total = 0;
                for (int requestPage = staPage; requestPage <= endPage; requestPage++) {
                    BlogSearchResult response = client.search(keyword, sort, requestPage);
                    allItems.addAll(response.getItems());
                    total = response.getTotal();
                }
                int sub = 50 * (staPage - 1);
                List<Blog> items = allItems.subList(sta - sub - 1, end - sub);
                return new Blogs(items, page, size, total);
            } catch (ApiServerErrorException | ApiResponseSchemaErrorException e) {
                log.error(e.getMessage(), e.getCause());
            }
        }
        throw new AllApiServerErrorException();
    }
}
