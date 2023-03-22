package com.example.search.blog;

import com.example.search.blog.client.BlogSearchClients;
import com.example.search.blog.client.ErrorSearchClient;
import com.example.search.blog.client.MockSearchClient;
import com.example.search.blog.client.error.AllApiServerErrorException;
import com.example.search.blog.exchange.SortType;
import com.example.search.keyword.message.KeywordCountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import java.util.List;

class BlogServiceTests {

    @Test
    @Description("01. 1번 Api 장애 시 2번 Api 동작")
    void _01_search() {
        KeywordCountService publisher = createKeywordCountPublisher();
        BlogSearchClients clients = new BlogSearchClients(
                List.of(
                        new ErrorSearchClient(1),
                        new MockSearchClient(2),
                        new MockSearchClient(3)
                )
        );
        BlogService service = new BlogService(publisher, clients);

        Blogs result = service.search("keyword", SortType.ACCURACY, 1, 10);

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(10);
        Assertions.assertThat(result.getTotal()).isEqualTo(200);
        Assertions.assertThat(result.getItems().get(0).getTitle()).isEqualTo("2");
    }

    @Test
    @Description("02. 전체 Api 장애")
    void _02_search() {
        KeywordCountService publisher = createKeywordCountPublisher();
        BlogSearchClients clients = new BlogSearchClients(
                List.of(
                        new ErrorSearchClient(1),
                        new ErrorSearchClient(2),
                        new ErrorSearchClient(3)
                )
        );
        BlogService service = new BlogService(publisher, clients);

        Assertions
                .assertThatThrownBy(() -> service.search("keyword", SortType.ACCURACY, 1, 10))
                .isInstanceOf(AllApiServerErrorException.class);
    }

    @Test
    @Description("03. Page, Size 테스트")
    void _03_search() {
        KeywordCountService publisher = createKeywordCountPublisher();
        BlogSearchClients clients = new BlogSearchClients(List.of(new MockSearchClient(1)));
        BlogService service = new BlogService(publisher, clients);

        Blogs result1 = service.search("keyword", SortType.ACCURACY, 1, 10);
        Assertions.assertThat(result1.getTotal()).isEqualTo(200);

        List<Blog> resultItems1 = result1.getItems();
        Assertions.assertThat(resultItems1).hasSize(10);
        Assertions.assertThat(resultItems1.get(0).getContents()).isEqualTo("01 keyword");
        Assertions.assertThat(resultItems1.get(9).getContents()).isEqualTo("10 keyword");

        Blogs result2 = service.search("keyword", SortType.ACCURACY, 2, 10);
        Assertions.assertThat(result2.getTotal()).isEqualTo(200);

        List<Blog> resultItems2 = result2.getItems();
        Assertions.assertThat(resultItems2).hasSize(10);
        Assertions.assertThat(resultItems2.get(0).getContents()).isEqualTo("11 keyword");
        Assertions.assertThat(resultItems2.get(9).getContents()).isEqualTo("20 keyword");

        Blogs result3 = service.search("keyword", SortType.ACCURACY, 2, 5);
        Assertions.assertThat(result3.getTotal()).isEqualTo(200);

        List<Blog> resultItems3 = result3.getItems();
        Assertions.assertThat(resultItems3).hasSize(5);
        Assertions.assertThat(resultItems3.get(0).getContents()).isEqualTo("06 keyword");
        Assertions.assertThat(resultItems3.get(4).getContents()).isEqualTo("10 keyword");

        Blogs result4 = service.search("keyword", SortType.ACCURACY, 3, 30);
        Assertions.assertThat(result3.getTotal()).isEqualTo(200);

        List<Blog> resultItems4 = result4.getItems();
        Assertions.assertThat(resultItems4).hasSize(30);
        Assertions.assertThat(resultItems4.get(0).getContents()).isEqualTo("61 keyword");
        Assertions.assertThat(resultItems4.get(29).getContents()).isEqualTo("90 keyword");

        Blogs result5 = service.search("keyword", SortType.ACCURACY, 4, 40);
        Assertions.assertThat(result3.getTotal()).isEqualTo(200);

        List<Blog> resultItems5 = result5.getItems();
        Assertions.assertThat(resultItems5).hasSize(40);
        Assertions.assertThat(resultItems5.get(0).getContents()).isEqualTo("121 keyword");
        Assertions.assertThat(resultItems5.get(39).getContents()).isEqualTo("160 keyword");

        Blogs result6 = service.search("keyword", SortType.ACCURACY, 10, 10);
        Assertions.assertThat(result3.getTotal()).isEqualTo(200);
        List<Blog> resultItems6 = result6.getItems();
        Assertions.assertThat(resultItems6).hasSize(10);
        Assertions.assertThat(resultItems6.get(0).getContents()).isEqualTo("91 keyword");
        Assertions.assertThat(resultItems6.get(9).getContents()).isEqualTo("100 keyword");
    }


    private KeywordCountService createKeywordCountPublisher() {
        return keyword -> System.out.println("keyword = " + keyword);
    }
}
