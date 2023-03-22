package com.example.search.blog.client.kakao;

import com.example.search.blog.Blog;
import com.example.search.blog.client.BlogSearchClient;
import com.example.search.blog.client.BlogSearchResult;
import com.example.search.blog.client.error.ApiRequestSchemaErrorException;
import com.example.search.blog.client.error.ApiResponseSchemaErrorException;
import com.example.search.blog.client.error.ApiServerErrorException;
import com.example.search.blog.client.kakao.model.KakaoDocument;
import com.example.search.blog.client.kakao.model.KakaoResponse;
import com.example.search.blog.exchange.SortType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@Order(1) // BlogSearchClient 중 첫번째 구현체
public class KakaoSearchClient implements BlogSearchClient {
    private static final String NAME = "Kakao";

    private final String url;
    private final String key;
    private final ObjectMapper mapper;

    public KakaoSearchClient(@Value("${blog.kakao.url}") String url, @Value("${blog.kakao.key}") String key) {
        this.url = url;
        this.key = key;

        ObjectMapper customMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        });

        customMapper.registerModule(module);
        this.mapper = customMapper;
    }

    @Override
    public String getClientName() {
        return NAME;
    }

    @Override
    public boolean checkHealth() throws ApiServerErrorException {
        // Todo Health Check Api
        return true;
    }

    @Override
    @Cacheable(value = "blogs.kakao", key = "#keyword + '|' + #sort + '|' + #page")
    public BlogSearchResult search(String keyword, SortType sort, int page) {
        log.info("KakaoSearchClient %s %s %d".formatted(keyword, sort, page));
        KakaoResponse response = requestToKakao(keyword, sort, page, 50);
        List<Blog> items = response.getDocuments().stream().map(KakaoDocument::toBlog).toList();
        long total = response.getMeta().getTotalCount();

        return new BlogSearchResult(items, total);
    }

    private KakaoResponse requestToKakao(String keyword, SortType sort, int page, int size) {
        RestTemplate template = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", keyword)
                .queryParam("sort", sort.toKakaoSort())
                .queryParam("page", page)
                .queryParam("size", size);
        URI uri = builder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK %s".formatted(key));

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = template.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpServerErrorException e) {
            throw new ApiServerErrorException(NAME);
        } catch (HttpClientErrorException e) {
            throw new ApiRequestSchemaErrorException(NAME, e);
        }
        String result = response.getBody();

        KakaoResponse kakaoResponse;
        try {
            kakaoResponse = mapper.readValue(result, KakaoResponse.class);
        } catch (JsonProcessingException e) {
            throw new ApiResponseSchemaErrorException(NAME, e);
        }

        return kakaoResponse;
    }
}
