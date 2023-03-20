package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.BlogSearchClients;
import com.example.search.blog.client.error.AllApiServerErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import com.example.search.keyword.message.KeywordCountPublisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class BlogServiceTests {

    @Test
    @Description("01. 1번 Api 장애 시 2번 Api 동작")
    void _01_search() {
        KeywordCountPublisher publisher = createKeywordCountPublisher();
        BlogSearchClients clients = new BlogSearchClients(
                List.of(
                        createMockErrorBlogSearchClient(1),
                        createMockBlogSearchClient(2),
                        createMockBlogSearchClient(3)
                )
        );
        BlogService service = new BlogService(publisher, clients);

        Page<Blog> result = service.search("keyword", SortType.ACCURACY, 1, 10);
        Assertions.assertThat(result.getContent().get(0).getTitle()).isEqualTo("2번 구현체");
    }

    @Test
    @Description("02. 전체 Api 장애")
    void _02_search() {
        KeywordCountPublisher publisher = createKeywordCountPublisher();
        BlogSearchClients clients = new BlogSearchClients(
                List.of(
                        createMockErrorBlogSearchClient(1),
                        createMockErrorBlogSearchClient(2),
                        createMockErrorBlogSearchClient(3)
                )
        );
        BlogService service = new BlogService(publisher, clients);

        Assertions
                .assertThatThrownBy(() -> service.search("keyword", SortType.ACCURACY, 1, 10))
                .isInstanceOf(AllApiServerErrorException.class);
    }

    private Page<Blog> createMockResult(String keyword, int page, int size) {
        List<Blog> items = new ArrayList<>();
        items.add(new Blog(keyword, keyword, keyword, keyword, keyword, LocalDateTime.now()));
        return new PageImpl<>(items, PageRequest.of(page, size), items.size());
    }

    private KeywordCountPublisher createKeywordCountPublisher() {
        return keyword -> System.out.println("keyword = " + keyword);
    }

    private BlogSearchClient createMockErrorBlogSearchClient(int order) {
        return (keyword, sort, page, size) -> {
            throw new ApiServerErrorException("%d번 구현체".formatted(order));
        };
    }

    private BlogSearchClient createMockBlogSearchClient(int order) {
        return (keyword, sort, page, size) -> createMockResult("%d번 구현체".formatted(order), page, size);
    }
}
